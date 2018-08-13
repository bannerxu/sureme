package top.xuguoliang.common.utils;

import com.thoughtworks.xstream.XStream;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import top.xuguoliang.service.payment.web.UnifiedOrderParam;
import top.xuguoliang.service.payment.web.UnifiedOrderResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jinguoguo
 */
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
