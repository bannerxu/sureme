package top.xuguoliang.service.order;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.common.utils.NumberUtil;
import top.xuguoliang.common.utils.PaymentUtil;
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.commodity.StockKeepingUnit;
import top.xuguoliang.models.commodity.StockKeepingUnitDao;
import top.xuguoliang.models.coupon.PersonalCoupon;
import top.xuguoliang.models.coupon.PersonalCouponDao;
import top.xuguoliang.models.order.*;
import top.xuguoliang.models.user.Address;
import top.xuguoliang.models.user.AddressDao;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.cart.web.ItemParamVO;
import top.xuguoliang.service.cart.web.OrderWebCartCreateParamVO;
import top.xuguoliang.service.cart.web.OrderWebCartCreateResultVO;
import top.xuguoliang.service.order.web.OrderWebCreateParamVO;
import top.xuguoliang.service.order.web.OrderWebDetailVO;
import top.xuguoliang.service.order.web.OrderWebResultVO;
import top.xuguoliang.service.payment.web.UnifiedOrderParam;
import top.xuguoliang.service.payment.web.UnifiedOrderResult;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class OrderWebService {

    private static final Logger logger = LoggerFactory.getLogger(OrderWebService.class);

    @Resource
    private CommonSpecUtil<Order> commonSpecUtil;

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private PersonalCouponDao personalCouponDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private UserDao userDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;

    @Resource
    private AddressDao addressDao;

    @Resource
    private PaymentUtil paymentUtil;

    /**
     * 创建订单
     *
     * @param orderWebCreateParamVO 订单创建需要的信息
     * @return 创建后的订单
     */
    public OrderWebResultVO createOrder(Integer userId, OrderWebCreateParamVO orderWebCreateParamVO) {
        Date date = new Date();
        // 返回值
        OrderWebResultVO vo = new OrderWebResultVO();
        // 订单实体
        Order order = new Order();

        // 从参数中取出有用信息
        OrderTypeEnum orderType = orderWebCreateParamVO.getOrderType();
        Integer addressId = orderWebCreateParamVO.getAddressId();
        Integer commodityId = orderWebCreateParamVO.getCommodityId();

        Address address = addressDao.findOne(addressId);
        if (ObjectUtils.isEmpty(address) || address.getDeleted()) {
            logger.error("调用创建订单业务失败：找不到对应的地址");
            throw new ValidationException(MessageCodes.WEB_ADDRESS_NOT_EXIST, "地址不存在，请重新选择地址");
        }

        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("调用创建订单业务失败：找不到对应的商品");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST, "商品不存在，请重新下单");
        }

        // 设置订单属性
        BeanUtils.copyNonNullProperties(address, order);
        BeanUtils.copyNonNullProperties(commodity, order);
        order.setOrderType(orderType);
        order.setCreateTime(date);
        order.setUpdateTime(date);
        order.setDeleted(false);
        order.setOrderNumber(generateOrderNumber());
        order.setUserId(userId);

        return vo;
    }

    /**
     * 生成订单号：时间戳 + 随机6位数字
     *
     * @return 订单号
     */
    private String generateOrderNumber() {
        String time = String.valueOf(System.currentTimeMillis());
        int number = (int) ((Math.random() * 9 + 1) * 100000);
        return time + number;
    }

    /**
     * 通过订单号查询订单
     *
     * @param orderNumber 订单号
     * @return 订单信息
     */
    public OrderWebResultVO findByOrderNumber(String orderNumber) {
        Order order = orderDao.findByOrderNumberEquals(orderNumber);
        if (ObjectUtils.isEmpty(order) || order.getDeleted()) {
            logger.warn("通过订单号查询订单业务错误：订单不存在或已被删除，编号：{}", orderNumber);
            throw new ValidationException(MessageCodes.WEB_ORDER_NOT_EXIST, "订单不存在");
        } else {
            OrderWebResultVO vo = new OrderWebResultVO();
            BeanUtils.copyNonNullProperties(order, vo);
            return vo;
        }
    }

    /**
     * 分页查询指定用户id的订单
     *
     * @param userId   用户id
     * @param pageable 分页信息
     * @return 订单信息
     */
    public Page<OrderWebResultVO> findPage(Integer userId, OrderStatusEnum orderStatus, Pageable pageable) {
        Specification<Order> specification = commonSpecUtil.equal("userId", userId);
        Specification<Order> status = commonSpecUtil.equal("orderStatus", orderStatus);
        Specifications<Order> where;

        if (orderStatus.equals(OrderStatusEnum.QUERY_ALL)) {
            where = Specifications.where(specification);
        } else {
            where = Specifications.where(specification).and(status);
        }

        return orderDao.findAll(where, pageable).map(order -> {
            OrderWebResultVO vo = new OrderWebResultVO();
            BeanUtils.copyNonNullProperties(order, vo);

            List<OrderItem> orderItems = orderItemDao.findByOrderIdIs(order.getOrderId());
            vo.setOrderItems(orderItems);
            return vo;
        });
    }

    /**
     * 购物车下单
     *
     * @param userId 用户id
     * @param vo     下单信息
     * @return Order
     */
    @Transactional(rollbackOn = {Exception.class})
    public OrderWebCartCreateResultVO createCartOrder(Integer userId, OrderWebCartCreateParamVO vo) {
        Date date = new Date();
        // 商品总价
        BigDecimal total = BigDecimal.valueOf(0.0);

        List<ItemParamVO> items = vo.getItems();
        for (ItemParamVO item : items) {
            Integer stockKeepingUnitId = item.getStockKeepingUnitId();
            Integer voCount = item.getSkuCount();
            BigDecimal skuCount = BigDecimal.valueOf(voCount);
            StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.findOne(stockKeepingUnitId);
            // 非空判断
            if (!ObjectUtils.isEmpty(stockKeepingUnit)) {
                // 判断库存是否足够
                Integer stock = stockKeepingUnit.getStock();
                if (voCount > stock) {
                    logger.error("购物车下单失败：规格库存不足");
                    throw new ValidationException(MessageCodes.WEB_SKU_STOCK_NOT_ENOUGH);
                }
                // 减少库存，保存
                stockKeepingUnit.setStock(stock - voCount);
                stockKeepingUnitDao.save(stockKeepingUnit);

                // 计算总价
                BigDecimal discountPrice = stockKeepingUnit.getDiscountPrice();
                BigDecimal itemPrice = discountPrice.multiply(skuCount);
                total = total.add(itemPrice);
            } else {
                logger.error("购物车下单失败：商品规格不存在（用户id：{}）", userId);
                throw new ValidationException(MessageCodes.WEB_SKU_NOT_EXIST);
            }
        }
        // 判断优惠券是否属于优惠范围
        Integer personalCouponId = vo.getPersonalCouponId();
        PersonalCoupon personalCoupon = personalCouponDao.findByPersonalCouponIdIsAndUserIdIsAndDeletedIsFalse(personalCouponId, userId);
        if (ObjectUtils.isEmpty(personalCoupon)) {
            logger.error("购物车下单失败：优惠券不存在（用户id:{}）", userId);
            throw new ValidationException(MessageCodes.WEB_COUPON_NOT_EXIST);
        }
        // 是否在优惠券使用时间内
        Date useBeginTime = personalCoupon.getUseBeginTime();
        Date useEndTime = personalCoupon.getUseEndTime();
        if (useBeginTime.after(date) || useEndTime.before(date)) {
            logger.error("购物车下单失败：优惠券不在使用时间内（用户id:{}）", userId);
            throw new ValidationException(MessageCodes.WEB_COUPON_CAN_NOT_USE);
        }
        // 商品总价是否达到优惠券满减价格
        BigDecimal minUseMoney = personalCoupon.getMinUseMoney();
        if (minUseMoney.doubleValue() > total.doubleValue()) {
            logger.error("购物车下单失败：商品总价未达到优惠券满减金额（用户id:{}）", userId);
            throw new ValidationException(MessageCodes.WEB_COUPON_NOT_ENOUGH);
        }
        // 计算需要支付的价格 TODO 运费待议
        BigDecimal needPayMoney = total.subtract(personalCoupon.getOffsetMoney());

        // 判断地址是否为空
        Integer addressId = vo.getAddressId();
        Address address = addressDao.findOne(addressId);
        if (ObjectUtils.isEmpty(address) || address.getDeleted()) {
            logger.error("购物车下单失败：地址不存在");
            throw new ValidationException(MessageCodes.WEB_ADDRESS_NOT_EXIST);
        }

        // 新建订单，减少库存，如果取消订单再行恢复
        Order order = new Order();
        BeanUtils.copyNonNullProperties(address, order);
        BeanUtils.copyNonNullProperties(personalCoupon, order);
        order.setUserId(userId);
        order.setOrderStatus(OrderStatusEnum.ORDER_WAITING_PAYMENT);
        order.setOrderNumber(NumberUtil.generateOrderNumber("co"));
        order.setOrderType(OrderTypeEnum.ORDER_TYPE_NORMAL);
        order.setCreateTime(date);
        order.setUpdateTime(date);
        order.setDeleted(false);
        order.setTotalMoney(total);

        return null;
    }

    /**
     * 统一下单
     *
     * @return 下单返回对象
     */
    private UnifiedOrderResult unifiedOrder() {
        // 调用微信统一下单接口，返回预支付对象
//        UnifiedOrderParam unifiedOrderParam = new UnifiedOrderParam();
//        unifiedOrderParam.setAppid();
//        unifiedOrderParam.setMch_id();
//        unifiedOrderParam.setNotify_url();
//        unifiedOrderParam.setOpenid();
//        UnifiedOrderResult unifiedOrderResult = paymentUtil.unifiedOrder();

        return null;
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单id
     * @return 订单详情
     */
    public OrderWebDetailVO getDetail(Integer userId, Integer orderId) {
        Order order = orderDao.findOne(orderId);
        if (ObjectUtils.isEmpty(order) || order.getDeleted()) {
            logger.error("查询订单详情失败：订单{} 不存在");
            throw new ValidationException(MessageCodes.WEB_ORDER_NOT_EXIST);
        }
        if (order.getUserId().equals(userId)) {
            logger.error("查询订单详情失败：当前用户{} 不是订单{} 的所有者");
        }

        OrderWebDetailVO detailVO = new OrderWebDetailVO();
        BeanUtils.copyNonNullProperties(order, detailVO);
        List<OrderItem> orderItems = orderItemDao.findByOrderIdIs(orderId);
        detailVO.setOrderItems(orderItems);

        return detailVO;
    }
}
