package top.xuguoliang.service.payment;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.brokerage.BrokerageWebService;

import javax.annotation.Resource;
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
    private PaymentUtil paymentUtil;

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserDao userDao;

    @Resource
    private MoneyWaterDao moneyWaterDao;

    /**
     * 统一下单
     * @param userId 用户id
     * @param orderId
     * @return
     */
    public WxPayUnifiedOrderResult unifiedOrder(Integer userId, Integer orderId) {

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
        if (!userId.equals(order.getOrderId())) {
            logger.error("统一下单失败：订单：{}和用户不匹配：{}", order, userId);
            throw new ValidationException(MessageCodes.WEB_ORDER_USER_NO_MATCH);
        }

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setOutTradeNo(order.getOrderNumber());
        orderRequest.setBody("小贝真商品购买");
        orderRequest.setOpenid(user.getOpenId());
        orderRequest.setSpbillCreateIp("127.0.0.1");
        orderRequest.setNotifyUrl("http://");

        WxPayService wxPayService = new WxPayServiceImpl();
        try {
            return wxPayService.unifiedOrder(orderRequest);
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
}
