package top.xuguoliang.service.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.common.utils.LogisticsUtil;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.commodity.StockKeepingUnitDao;
import top.xuguoliang.models.logistics.LogisticsRecord;
import top.xuguoliang.models.logistics.LogisticsRecordDao;
import top.xuguoliang.models.order.*;
import top.xuguoliang.service.RedisKeyPrefix;
import top.xuguoliang.service.order.cms.OrderCmsResultVO;
import top.xuguoliang.service.order.cms.OrderSendParamVO;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
     * @param pageable 分页信息
     * @return 分页订单
     */
    public Page<OrderCmsResultVO> findPage(OrderStatusEnum orderStatus, Pageable pageable) {
        Specification<Order> specification = commonSpecUtil.equal("orderStatus", orderStatus);
        Page<Order> orders = orderDao.findAll(specification, pageable);

        return orders.map(this::convertOrderToVO);
    }

    /**
     * 查询单个订单
     *
     * @param orderId 订单id
     * @return 订单结果
     */
    public OrderCmsResultVO getOrder(Integer orderId) {

        Order order = orderDao.findOne(orderId);
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
        List<OrderItem> orderItems = orderItemDao.findByOrderIdIs(orderId);
        vo.setOrderItems(orderItems);
        return vo;
    }

    /**
     * 删除订单（只能删除用户已经取消的）
     *
     * @param orderId 订单id
     */
    public void deleteOrder(Integer orderId) {
        Order order = orderDao.findOne(orderId);
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
        // 发货时，修改发货时间，订单状态
        Order order = orderDao.findOne(orderId);
        if (ObjectUtils.isEmpty(order) || order.getDeleted()) {
            logger.error("订单发货失败：订单{} 不存在", orderId);
            throw new ValidationException(MessageCodes.CMS_ORDER_NOT_EXIST);
        }

        String logisticsNumber = vo.getLogisticsNumber();

        order.setLogisticsNumber(logisticsNumber);
        order.setLogisticsCompany(vo.getLogisticsCompany());
        orderDao.saveAndFlush(order);
    }

    /**
     * 获取物流信息
     *
     * @param orderId 订单id
     * @return 物流信息
     */
    public String getLogisticsInfo(Integer orderId) {
        Order order = orderDao.findOne(orderId);
        String logisticsNumber = order.getLogisticsNumber();
        String logisticsCompany = order.getLogisticsCompany();

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String logisticsInfo = valueOperations.get(RedisKeyPrefix.logisticsInfo(orderId));
        if (StringUtils.isEmpty(logisticsInfo)) {
            String info = logisticsUtil.getLogisticsInfo(logisticsNumber, logisticsCompany);
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
}
