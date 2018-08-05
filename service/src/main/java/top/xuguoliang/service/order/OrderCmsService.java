package top.xuguoliang.service.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.commodity.StockKeepingUnitDao;
import top.xuguoliang.models.order.Order;
import top.xuguoliang.models.order.OrderDao;
import top.xuguoliang.models.order.OrderStatusEnum;
import top.xuguoliang.service.order.cms.OrderCmsPageParamVO;
import top.xuguoliang.service.order.cms.OrderCmsResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class OrderCmsService {

    private static final Logger logger = LoggerFactory.getLogger(OrderCmsService.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;

    @Resource
    private CommonSpecUtil<Order> commonSpecUtil;

    public Page<OrderCmsResultVO> findPage(OrderCmsPageParamVO orderCmsPageParamVO) {
        OrderStatusEnum orderStatus = orderCmsPageParamVO.getOrderStatus();
        Pageable pageable = orderCmsPageParamVO.getPageable();
        Specification<Order> specification = commonSpecUtil.equal("orderStatus", orderStatus);
        Page<Order> orders = orderDao.findAll(specification, pageable);
        Page<OrderCmsResultVO> vos = orders.map(order -> {
            OrderCmsResultVO vo = new OrderCmsResultVO();
            BeanUtils.copyNonNullProperties(order, vo);
            return vo;
        });

        return vos;
    }

    /**
     * 查询单个订单
     *
     * @param orderId 订单id
     * @return 订单结果
     */
    public OrderCmsResultVO getOrder(Integer orderId) {
        // 返回值
        OrderCmsResultVO vo = new OrderCmsResultVO();

        Order order = orderDao.findOne(orderId);
        if (ObjectUtils.isEmpty(order)) {
            logger.error("调用订单单个查询业务：id对应的订单不存在");
            throw new ValidationException(MessageCodes.CMS_ORDER_NOT_EXIST, "订单不存在");
        }

        BeanUtils.copyNonNullProperties(order, vo);

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
}
