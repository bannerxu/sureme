package top.banner.service.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.banner.common.exception.MessageCodes;
import top.banner.common.exception.ValidationException;
import top.banner.common.utils.*;
import top.banner.models.commodity.CommodityBanner;
import top.banner.models.commodity.CommodityBannerDao;
import top.banner.models.commodity.CommodityDao;
import top.banner.models.commodity.StockKeepingUnitDao;
import top.banner.models.logistics.LogisticsRecord;
import top.banner.models.logistics.LogisticsRecordDao;
import top.banner.models.moneywater.MoneyWater;
import top.banner.models.moneywater.MoneyWaterType;
import top.banner.models.order.*;
import top.banner.service.RedisKeyPrefix;
import top.banner.service.order.cms.OrderCmsResultVO;
import top.banner.service.order.cms.OrderSendParamVO;
import top.banner.service.order.web.OrderItemVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static top.banner.common.utils.ExcelHelper.WEBAPP_EXPORT_EXCEL;

/**
 * @author jinguoguo
 */
@Service
public class OrderCmsService {

    private static final Logger logger = LoggerFactory.getLogger(OrderCmsService.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

    @Resource
    private LogisticsRecordDao logisticsRecordDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;

    @Resource
    private CommonSpecUtil<Order> commonSpecUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private LogisticsUtil logisticsUtil;

    /**
     * 分页查询订单
     *
     * @param orderStatus 订单状态
     * @param pageable    分页信息
     * @return 分页订单
     */
    public Page<OrderCmsResultVO> findPage(OrderStatusEnum orderStatus, Pageable pageable) {
        Specification<Order> specification = commonSpecUtil.equal("orderStatus", orderStatus);
        Specification<Order> deleted = commonSpecUtil.equal("deleted", false);
        Specifications<Order> specifications = null;
        if (ObjectUtils.isEmpty(orderStatus)) {
            specifications = Specifications.where(deleted);
        } else {
            specifications = Specifications.where(specification).and(deleted);
        }
        Page<Order> orders = orderDao.findAll(specifications, pageable);

        return orders.map(this::convertOrderToVO);
    }

    /**
     * 查询单个订单
     *
     * @param orderId 订单id
     * @return 订单结果
     */
    public OrderCmsResultVO getOrder(Integer orderId) {

        Order order = orderDao.getOne(orderId);
        if (ObjectUtils.isEmpty(order)) {
            logger.error("调用订单单个查询业务：id{}对应的订单不存在", orderId);
            throw new ValidationException(MessageCodes.CMS_ORDER_NOT_EXIST, "订单不存在");
        }

        return convertOrderToVO(order);
    }

    /**
     * 订单转VO
     *
     * @param order 订单
     * @return VO
     */
    private OrderCmsResultVO convertOrderToVO(Order order) {
        OrderCmsResultVO vo = new OrderCmsResultVO();
        BeanUtils.copyNonNullProperties(order, vo);
        Integer orderId = order.getOrderId();
        List<OrderItemVO> orderItems = orderItemDao.findByOrderIdIs(orderId).stream().map(orderItem -> {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyNonNullProperties(orderItem, orderItemVO);
            Integer commodityId = orderItem.getCommodityId();
            List<CommodityBanner> commodityBanners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
            orderItemVO.setCommodityBanners(commodityBanners);
            return orderItemVO;
        }).collect(Collectors.toList());
        vo.setOrderItems(orderItems);
        return vo;
    }

    /**
     * 删除订单（只能删除用户已经取消的）
     *
     * @param orderId 订单id
     */
    public void deleteOrder(Integer orderId) {
        Order order = orderDao.getOne(orderId);
        OrderStatusEnum orderStatus = order.getOrderStatus();
        // 只有订单为已退款、已退货时，才执行删除
        if (orderStatus.equals(OrderStatusEnum.ORDER_RETURNED) || orderStatus.equals(OrderStatusEnum.ORDER_REFUNDED)) {
            order.setDeleted(true);
            orderDao.saveAndFlush(order);
        } else {
            logger.warn("订单目前状态非可删除，导致删除订单失败");
        }
    }

    /**
     * 发货
     *
     * @param orderId 订单id
     * @param vo      物流信息
     */
    public void send(Integer orderId, OrderSendParamVO vo) {
        Date date = new Date();
        // 发货时，修改发货时间，订单状态
        Order order = orderDao.getOne(orderId);
        if (ObjectUtils.isEmpty(order) || order.getDeleted()) {
            logger.error("订单发货失败：订单{} 不存在", orderId);
            throw new ValidationException(MessageCodes.CMS_ORDER_NOT_EXIST);
        }

        // 判断：只有在订单是已支付状态才能发货
        OrderStatusEnum orderStatus = order.getOrderStatus();
        if (!orderStatus.equals(OrderStatusEnum.ORDER_WAITING_SEND)) {
            throw new ValidationException(MessageCodes.ORDER_STATUS_NOT_MATCH);
        }

        String logisticsNumber = vo.getLogisticsNumber();

        order.setLogisticsNumber(logisticsNumber);
        order.setLogisticsCompany(vo.getLogisticsCompany());
        order.setOrderStatus(OrderStatusEnum.ORDER_SENT);
        order.setSendTime(date);
        orderDao.saveAndFlush(order);
    }

    /**
     * 获取物流信息
     *
     * @param orderId 订单id
     * @return 物流信息
     */
    public String getLogisticsInfo(Integer orderId) {
        Order order = orderDao.getOne(orderId);
        String logisticsNumber = order.getLogisticsNumber();
        String logisticsCompany = order.getLogisticsCompany();

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String logisticsInfo = valueOperations.get(RedisKeyPrefix.logisticsInfo(orderId));
        if (StringUtils.isEmpty(logisticsInfo)) {
            String info = logisticsUtil.getLogisticsInfo(logisticsCompany, logisticsNumber);
            // 存入redis
            valueOperations.set(RedisKeyPrefix.logisticsInfo(orderId), info, 1, TimeUnit.HOURS);
            LogisticsRecord logisticsRecord = logisticsRecordDao.findByOrderIdIs(orderId);
            if (ObjectUtils.isEmpty(logisticsRecord)) {
                logisticsRecord = new LogisticsRecord();
                logisticsRecord.setOrderId(orderId);
                logisticsRecord.setLogisticsNumber(logisticsNumber);
                logisticsRecord.setLogisticsInfo(info);
                logisticsRecord.setLogisticsCompany(logisticsCompany);
                logisticsRecordDao.saveAndFlush(logisticsRecord);
            }
            return info;
        }
        return logisticsInfo;
    }

    public void excel(HttpServletResponse response) throws Exception {


        List<MoneyWater> list = new ArrayList<>();
        MoneyWater moneyWater = new MoneyWater();
        moneyWater.setMoney(BigDecimal.TEN);
        moneyWater.setDeleted(false);
        moneyWater.setTime(new Date());
        moneyWater.setType(MoneyWaterType.PAY);
        moneyWater.setMoneyWaterId(1);
        moneyWater.setUserId(1);
        moneyWater.setOrderId(1);
        list.add(moneyWater);

        List<String> titles = Arrays.asList("金钱", "是否删除", "时间", "时间", "时间", "时间", "时间");


        ExcelHelper.writeExcel("1.xls",list,MoneyWater.class,new ArrayList<>(1),titles);

        ExportUtil.exportToClient(response, "multipart/form-data", WEBAPP_EXPORT_EXCEL, "1.xls");
    }
}
