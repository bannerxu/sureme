package top.xuguoliang.controllers;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.xuguoliang.service.payment.PaymentWebService;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/payment")
@Api(tags = "支付模块")
public class PaymentController {

    @Resource
    private PaymentWebService paymentWebService;

    @PostMapping("unifiedOrder")
    @ApiOperation("订单统一下单")
    public WxPayUnifiedOrderResult unifiedOrder(Integer orderId) {
        Integer userId = UserHelper.getUserId();
        return paymentWebService.unifiedOrder(userId, orderId);
    }

    @ApiOperation("支付回调")
    @RequestMapping(value = "payOrderNotify", method = {RequestMethod.POST, RequestMethod.GET})
    public void payOrderNotify(WxPayOrderNotifyResult wxPayOrderNotifyResult) {

        // TODO: 2018-08-19 支付回调，如果成功要创建佣金记录
        paymentWebService.payOrderNotify(wxPayOrderNotifyResult);
    }
}
