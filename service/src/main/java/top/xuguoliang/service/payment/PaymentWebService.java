package top.xuguoliang.service.payment;

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
import top.xuguoliang.models.order.Order;
import top.xuguoliang.models.order.OrderDao;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class PaymentWebService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentWebService.class);

    @Resource
    private PaymentUtil paymentUtil;

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserDao userDao;

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
}