package top.banner.common.utils;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.thoughtworks.xstream.XStream;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.banner.service.payment.web.UnifiedOrderParam;
import top.banner.service.payment.web.UnifiedOrderResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jinguoguo
 */
@Component
public class PaymentUtil {

    private final Logger logger = LoggerFactory.getLogger(WeChatUtil.class);

    @Value("${wx.url}")
    private String url;

    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Value("${wx.pay.mch_id}")
    private String mchId;


    public WxPayUnifiedOrderResult unified(WxPayUnifiedOrderRequest orderRequest) throws WxPayException {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setNotifyUrl("");
        orderRequest.setBody("主题");
        orderRequest.setOutTradeNo("订单号");
//        //元转成分
//        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen("1"));
        // 这个最好换成本机的真实ip
//        orderRequest.setSpbillCreateIp("127.0.0.1");
//        orderRequest.setTimeStart("yyyyMMddHHmmss");
//        orderRequest.setTimeExpire("yyyyMMddHHmmss");
        orderRequest.setTradeType("JSAPI");

        WxPayService wxPayService = new WxPayServiceImpl();
        return wxPayService.unifiedOrder(orderRequest);
    }

    public UnifiedOrderResult unifiedOrder(UnifiedOrderParam unifiedOrderParam) {
        Map<String, String> resultMap = new HashMap<>();
        //请求参数
        String unifiedOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        XStream xStream = XMLUtil2.getXStreamWithoutCdata();
        xStream.alias("xml", UnifiedOrderParam.class);
        String xml = xStream.toXML(unifiedOrderParam);
        logger.debug("统一下单接口参数： {}", xml);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml"), xml);
        //发送请求
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(unifiedOrderUrl).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        UnifiedOrderResult unifiedOrderResult;
        try {
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            logger.debug("统一下单返回 responseBody： {}", responseBody);
            String responseStr = responseBody.string();
            logger.debug("统一下单返回 responseStr {}", responseStr);

            xStream = XMLUtil2.getXStream();
            xStream.alias("xml", UnifiedOrderResult.class);
            unifiedOrderResult = (UnifiedOrderResult) xStream.fromXML(responseStr);


            logger.info("统一下单 Result 对象： {}", unifiedOrderResult);

            return unifiedOrderResult;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
