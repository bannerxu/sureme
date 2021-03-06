package top.banner.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 袁雾头 on 2017/7/6.
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static PoolingHttpClientConnectionManager cm;
    private static String UTF_8 = "UTF-8";

    private static void init() {
        if (cm == null) {
            synchronized (HttpUtils.class) {
                if (cm == null) {
                    cm = new PoolingHttpClientConnectionManager();
                    cm.setMaxTotal(30);// 整个连接池最大连接数
                    cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
                }
            }
        }
    }

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * Get请求不带参数
     *
     * @param url
     * @return
     */
    public static String httpGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }

    /**
     * Get请求带参数
     *
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     */
    public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException {
        logger.info(url);
        URIBuilder ub = new URIBuilder(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);
        logger.info(ub.build().toString());
        HttpGet httpGet = new HttpGet(ub.build().toString());
        return getResult(httpGet);
    }

    /**
     * Get请求带参数.头部参数
     *
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     */
    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet);
    }

    /**
     * post请求不带参数
     *
     * @param url
     * @return
     */
    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }

    /**
     * post请求带参数
     *
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost);
    }

    /**
     * post请求
     *
     * @param url       url地址
     * @param jsonParam json参数
     */
    public static String httpPost(String url, JSONObject jsonParam) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
                CloseableHttpResponse response = httpClient.execute(method);
                String responseMessage = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info(" --------消息返回内容为 ---------" + responseMessage);
                if (!responseMessage.contains("ok")) {
                    logger.info(" --------消息发送失败 ---------" + responseMessage);
                }
                return responseMessage;
            }
        } catch (IOException e) {
            logger.error(" httpPost error ");
        }
        return null;
    }

    /**
     * post请求
     *
     * @param url       url地址
     * @param jsonParam 参数
     * @return JSONObject
     */
    public static String httpPostAndReturnResult(String url, JSONObject jsonParam) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
                CloseableHttpResponse response = httpClient.execute(method);
                String responseMessage = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info(" --------消息返回内容为 ---------" + responseMessage);
                return responseMessage;
            }
        } catch (IOException e) {
            logger.error(" httpPost error ");
        }
        return null;
    }

    /**
     * post请求带参数，头部参数
     *
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

        return getResult(httpPost);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }

        return pairs;
    }

    /**
     * 处理Http请求
     *
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request) {
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            // response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                logger.info("--------------------get result success--------------------");
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity, "utf-8");
                logger.info("---------------------{}-------------", result);
                response.close();
                return result;
            }
        } catch (IOException e) {
            logger.error(" HttpUtils getResult() error ", e);
        }
        return null;
    }

}
