package top.xuguoliang.service.payment;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.PaymentUtil;
import top.xuguoliang.models.moneywater.MoneyWater;
import top.xuguoliang.models.moneywater.MoneyWaterDao;
import top.xuguoliang.models.moneywater.MoneyWaterType;
import top.xuguoliang.models.order.Order;
import top.xuguoliang.models.order.OrderDao;
import top.xuguoliang.models.order.OrderStatusEnum;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.brokerage.BrokerageWebService;
import top.xuguoliang.service.payment.web.RefundParam;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Service
public class PaymentWebService {
    @Resource
    private BrokerageWebService brokerageWebService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentWebService.class);

    @Resource
    private OrderDao orderDao;
    @Resource
    private UserDao userDao;
    @Resource
    private MoneyWaterDao moneyWaterDao;

    @Resource
    private WxPayService wxPayService;

    private static final String SUCCESS = "SUCCESS";

    /**
     * 统一下单
     *
     * @param userId  用户id
     * @param orderId 订单id
     * @return
     */
    public WxPayMpOrderResult unifiedOrder(Integer userId, Integer orderId) {

        // 判断用户是否存在
        User user = userDao.findOne(userId);
        if (ObjectUtils.isEmpty(user) || user.getDeleted()) {
            logger.error("统一下单失败：用户不存在：{}", userId);
            throw new ValidationException(MessageCodes.WEB_USER_NOT_EXIST);
        }

        // 判断订单是否存在
        Order order = orderDao.findOne(orderId);
        if (ObjectUtils.isEmpty(order) || order.getDeleted()) {
            logger.error("统一下单失败：订单不存在：{} | 当前用户：{}", orderId, userId);
            throw new ValidationException(MessageCodes.WEB_ORDER_NOT_EXIST);
        }

        // 判断订单是不是当前用户的
        if (!userId.equals(order.getUserId())) {
            logger.error("统一下单失败：订单：{}和用户不匹配：{}", orderId, userId);
            throw new ValidationException(MessageCodes.WEB_ORDER_USER_NO_MATCH);
        }

        WxPayUnifiedOrderRequest orderRequest = WxPayUnifiedOrderRequest.newBuilder()
                .outTradeNo(order.getOrderNumber())
                .body("小贝真商品购买")
                .openid(user.getOpenId())
                .spbillCreateIp("127.0.0.1")
                .notifyUrl("https://sureme-web.suremeshop.com/api/payment/unifiedOrderNotify")
                .totalFee(order.getRealPayMoney().multiply(new BigDecimal("100")).intValue())
                .tradeType("JSAPI")
                .build();

        try {
            WxPayMpOrderResult orderResult = wxPayService.createOrder(orderRequest);
            logger.debug("WxPayUnifiedOrderResult:------->{}", orderResult.toString());
            return orderResult;
        } catch (WxPayException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 校验回调
     *
     * @param wxPayOrderNotifyResult
     */
    public void payOrderNotify(WxPayOrderNotifyResult wxPayOrderNotifyResult) {

        //如果成功，就创建佣金记录

        brokerageWebService.insert(wxPayOrderNotifyResult.getOutTradeNo());
    }

    /**
     * 记录资金流水
     *
     * @param wxPayOrderNotifyResult 支付回调数据
     */
    public void addMoneyWater(WxPayOrderNotifyResult wxPayOrderNotifyResult) throws ParseException {
        String outTradeNo = wxPayOrderNotifyResult.getOutTradeNo();
        Integer totalFee = wxPayOrderNotifyResult.getTotalFee() * 100;

        BigDecimal totalMoney = BigDecimal.valueOf(totalFee);
        String timeEnd = wxPayOrderNotifyResult.getTimeEnd();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmms");
        Date payTime = simpleDateFormat.parse(timeEnd);

        // 用户
        User user = userDao.findByOpenId(wxPayOrderNotifyResult.getOpenid());

        MoneyWater moneyWater = new MoneyWater();
        moneyWater.setTime(payTime);
        moneyWater.setMoney(totalMoney);
        moneyWater.setType(MoneyWaterType.PAY);
        moneyWater.setUserId(user.getUserId());
        moneyWaterDao.save(moneyWater);
    }

    /**
     * 微信申请退款
     *
     * @param refundParam 订单id
     */
    public WxPayRefundResult refund(RefundParam refundParam) {
        Order order = orderDao.findOne(refundParam.getOrderId());
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        String orderNumber = order.getOrderNumber();

        // 退款参数组装
        wxPayRefundRequest.setOutRefundNo(orderNumber);
        wxPayRefundRequest.setOutTradeNo(orderNumber);
        wxPayRefundRequest.setRefundFee(order.getRealPayMoney().multiply(BigDecimal.valueOf(100L)).intValue());
        wxPayRefundRequest.setNotifyUrl("https://sureme-web.suremeshop.com/api/payment/refundNotify");

        try {
            return wxPayService.refund(wxPayRefundRequest);
        } catch (WxPayException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 统一下单回调
     *
     * @param xmlData 回调参数
     * @return 回调响应
     */
    @Transactional(rollbackOn = Exception.class)
    public String unifiedOrderNotify(String xmlData) {
        // 解析xml回调数据，返回对象
        try {
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlData);
            logger.debug("订单回调业务方法：解析回调参数：\n{}", result);
            if (ObjectUtils.isEmpty(result)) {
                String fail = WxPayNotifyResponse.fail("Fail");
                logger.debug("回调返回值为null");
                return fail;
            }

            String ok = WxPayNotifyResponse.success("OK");
            logger.debug("回调返回值：{}", ok);
            if (SUCCESS.equals(result.getReturnCode())) {

                String outTradeNo = result.getOutTradeNo();
                Integer totalFee = result.getTotalFee();
                Order order = orderDao.findByOrderNumberEquals(outTradeNo);

                if (ObjectUtils.isEmpty(order) || order.getDeleted()) {
                    // 订单不存在，打印错误
                    logger.error("退款失败：订单号{} 不存在", outTradeNo);
                    return WxPayNotifyResponse.fail("Fail");
                }

                if (order.getOrderStatus().equals(OrderStatusEnum.ORDER_WAITING_SEND)) {
                    // 防止重复调用
                    return ok;
                }

                // 支付成功，设置订单状态（已支付，待发货）
                order.setOrderStatus(OrderStatusEnum.ORDER_WAITING_SEND);
                orderDao.save(order);
                // 支付成功，设置佣金记录
                logger.debug("-> 支付成功：设置资金流水");
                // 记录资金流水
                addMoneyWater(result);

                // 支付成功，添加用户积分
                logger.debug("-> 支付成功：设置用户积分");
                User user = userDao.findOne(order.getUserId());
                user.setIntegral(user.getIntegral() + totalFee / 1000);
                userDao.save(user);

                // 返佣给上级用户（创建佣金记录）
                logger.debug("-> 支付成功：设置佣金记录");
                brokerageWebService.insert(outTradeNo);
            } else {
                // 付款失败
                logger.warn("用户付款失败：{}", result.getReturnMsg());
                return WxPayNotifyResponse.fail("Fail");
            }

            return ok;
        } catch (WxPayException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微信申请退款回调
     *
     * @param xmlData 回调参数
     * @return 处理结果
     */
    @Transactional(rollbackOn = Exception.class)
    public String refundNotify(String xmlData) {
        Date date = new Date();
        try {
            WxPayRefundNotifyResult result = wxPayService.parseRefundNotifyResult(xmlData);
            if (ObjectUtils.isEmpty(result)) {
                logger.error("退款失败：申请退款回调返回值为null");
                return WxPayNotifyResponse.fail("Fail");
            }
            if (SUCCESS.equals(result.getReturnCode())) {
                String outTradeNo = result.getReqInfo().getOutTradeNo();
                Integer refundFee = result.getReqInfo().getRefundFee();

                Order order = orderDao.findByOrderNumberEquals(outTradeNo);
                // 订单不存在，打印错误
                if (ObjectUtils.isEmpty(order) || order.getDeleted()) {
                    logger.error("退款失败：订单号{} 不存在", outTradeNo);
                    return WxPayNotifyResponse.fail("Fail");
                }

                OrderStatusEnum orderStatus = order.getOrderStatus();
                if (orderStatus.equals(OrderStatusEnum.ORDER_REFUNDED) || orderStatus.equals(OrderStatusEnum.ORDER_RETURNED)) {
                    // 防止重复回调
                    return WxPayNotifyResponse.success("ok");
                }

                // 记录资金流水
                logger.debug("退款回调：记录资金流水");
                MoneyWater moneyWater = new MoneyWater();
                moneyWater.setTime(date);
                moneyWater.setOrderId(order.getOrderId());
                moneyWater.setDeleted(false);
                moneyWater.setType(MoneyWaterType.REFUND);
                BigDecimal money = new BigDecimal(refundFee + "").divide(new BigDecimal("100"));
                moneyWater.setMoney(money);
                moneyWater.setUserId(order.getUserId());
                moneyWaterDao.save(moneyWater);

                // 设置订单状态
                logger.debug("退款回调：设置订单状态");
                order.setOrderStatus(OrderStatusEnum.ORDER_REFUNDED);
                orderDao.save(order);

                return WxPayNotifyResponse.success("ok");
            } else {
                // 退款失败，打印失败原因
                logger.error("退款失败：{}", result.getReturnMsg());
                return WxPayNotifyResponse.fail("Fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
