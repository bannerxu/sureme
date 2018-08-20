package top.xuguoliang.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import top.xuguoliang.service.logistics.LogisticsCompanyResult;
import top.xuguoliang.service.logistics.LogisticsInfoResult;

import java.io.IOException;

/**
 * 物流信息查询工具
 *
 * @author jinguoguo
 */
@Component
public class LogisticsUtil {

    private static final String APPKEY = "9bb8811207cb4878e6db95b40d038878";

    /**
     * 查询快递公司信息
     *
     * @return 快递公司信息
     */
    public LogisticsCompanyResult getLogisticsCompanyInfo() {
        String url = "http://v.juhe.cn/exp/com" + "?key=" + APPKEY;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        String resultJson = null;
        LogisticsCompanyResult logisticsCompanyResult = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resultJson = EntityUtils.toString(entity);
                // 将字符串转为对象
                logisticsCompanyResult = JSONObject.parseObject(resultJson, LogisticsCompanyResult.class);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logisticsCompanyResult;
    }

    /**
     * 根据快递公司编号和物流单号查询快递信息
     *
     * @param com 快递公司编号 如sf
     * @param no  物流单号
     * @return 快递信息
     */
    public LogisticsInfoResult getLogisticsInfo(String com, String no) {
        String url = "http://v.juhe.cn/exp/index" + "?com=" + com + "&no=" + no + "&dtype=&key=" + APPKEY;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        String resultJson = null;
        LogisticsInfoResult logisticsInfoResult = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resultJson = EntityUtils.toString(entity);
                // 将字符串转为对象
                logisticsInfoResult = JSONObject.parseObject(resultJson, LogisticsInfoResult.class);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return logisticsInfoResult;
    }
}
