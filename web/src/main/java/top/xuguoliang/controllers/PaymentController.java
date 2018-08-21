package top.xuguoliang.controllers;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.payment.PaymentWebService;
import top.xuguoliang.service.payment.web.UnifiedOrderParam;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/payment")
@Api(tags = "支付模块")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Resource
    private WxPayService wxService;

    @Resource
    private PaymentWebService paymentWebService;

    @PostMapping("unifiedOrder")
    @ApiOperation("订单统一下单")
    public WxPayUnifiedOrderResult unifiedOrder(@RequestBody UnifiedOrderParam unifiedOrderParam) {
        Integer userId = UserHelper.getUserId();
        return paymentWebService.unifiedOrder(userId, unifiedOrderParam.getOrderId());
    }

    @ApiOperation("支付回调")
    @RequestMapping(value = "payOrderNotify", method = {RequestMethod.POST, RequestMethod.GET})
    public WxPayNotifyResponse payOrderNotify(@RequestBody String xmlData) {

        // 解析xml回调数据，返回对象
        WxPayOrderNotifyResult wxPayOrderNotifyResult = null;
        try {
            wxPayOrderNotifyResult = this.wxService.parseOrderNotifyResult(xmlData);
        } catch (WxPayException e) {
            e.printStackTrace();
        }
        if ("SUCCESS".equals(wxPayOrderNotifyResult.getReturnCode())) {

        }



        // TODO: 2018-08-19 支付回调，如果成功要创建佣金记录
        paymentWebService.payOrderNotify(wxPayOrderNotifyResult);
        // 记录资金流水
        try {
            paymentWebService.addMoneyWater(wxPayOrderNotifyResult);
        } catch (ParseException e) {
            logger.warn("----------------记录资金流水错误-----------------");
            e.printStackTrace();
        }
        return null;
    }
}
