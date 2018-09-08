package top.xuguoliang.controllers;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.payment.PaymentWebService;
import top.xuguoliang.service.payment.web.RefundParam;
import top.xuguoliang.service.payment.web.UnifiedOrderParam;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Controller
@RequestMapping("api/payment")
@Api(tags = "支付模块")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Resource
    private PaymentWebService paymentWebService;

    @PostMapping("unifiedOrder")
    @ApiOperation("订单统一下单")
    public WxPayUnifiedOrderResult unifiedOrder(@RequestBody UnifiedOrderParam unifiedOrderParam) {
        Integer userId = UserHelper.getUserId();
        return paymentWebService.unifiedOrder(userId, unifiedOrderParam.getOrderId());
    }

    @ApiOperation("支付回调")
    @RequestMapping(value = "unifiedOrderNotify", method = {RequestMethod.POST, RequestMethod.GET})
    public String unifiedOrderNotify(@RequestBody String xmlData) {
        logger.debug("支付回调接口被调用：参数为：\n{}", xmlData);
        return paymentWebService.unifiedOrderNotify(xmlData);


    }

    public void refund(@RequestBody RefundParam refundParam) {
        paymentWebService.refund(refundParam);
    }


    @ApiOperation("申请退款回调")
    @RequestMapping(value = "refundNotify", method = {RequestMethod.POST, RequestMethod.GET})
    public String refundNotify(@RequestBody String xmlData) {
        return paymentWebService.refundNotify(xmlData);
    }

}
