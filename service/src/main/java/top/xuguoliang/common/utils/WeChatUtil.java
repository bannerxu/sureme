package top.xuguoliang.common.utils;


import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import top.xuguoliang.service.user.web.AuthorizeVO;
import top.xuguoliang.service.user.web.WeChatUser;


import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class WeChatUtil {

    //todo : 修改配置

    private final Logger logger = LoggerFactory.getLogger(WeChatUtil.class);

    @Value("${wx.url}")
    private String url;

    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Value("${wx.mchId}")
    private String mchId;

    @Value("${wx.mchKey}")
    private String mchKey;

    @Resource
    private SSL ssl;
    @Resource
    private XMLUtil xmlUtil;

    /**
     * wx.login授权获得用户信息
     */
    public WeChatUser login(String code) {
        //请求参数
        String params = "appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        String requestURL = url + "?" + params;
        //发送请求
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(requestURL).build();
        Call call = okHttpClient.newCall(request);

        WeChatUser userInfo = null;
        try {
            Response res = call.execute();
            ResponseBody responseBody = res.body();
            logger.info(" responseBody is {}", responseBody);
            Assert.notNull(responseBody, "登录失败");

            //解析响应内容（转换成json对象）
            String response = responseBody.string();
            logger.info(" response is {} ", response);
            Map<String, Object> userInfoMap = JsonUtils.json2object(response, Map.class, String.class, Object.class);
            logger.info(" userInfoMap is {} ", userInfoMap.toString());
            if (!userInfoMap.isEmpty()) {
                userInfo = new WeChatUser();
                userInfo.setOpenId(userInfoMap.get("openid").toString());
            }
        } catch (IOException e) {
            logger.error(" login get user info error {}", e);
        }
        return userInfo;
    }

    /**
     * wx.authorize授权获得用户信息
     *
     * @param authorizeVO
     * @return userInfo
     */
    @Transactional
    public WeChatUser authorize(AuthorizeVO authorizeVO) {
        //请求参数
        String params = "appid=" + appId + "&secret=" + appSecret + "&js_code=" + authorizeVO.getCode() + "&grant_type=authorization_code";
        String requestURL = url + "?" + params;
        //发送请求
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(requestURL).build();
        Call call = okHttpClient.newCall(request);

        WeChatUser userInfo = null;
        try {
            Response res = call.execute();
            ResponseBody responseBody = res.body();
            logger.info(" responseBody is {}", responseBody);
            Assert.notNull(responseBody, "授权失败");

            String response = responseBody.string();
            //解析响应内容（转换成json对象）
            Map<String, Object> map = JsonUtils.json2object(response, Map.class, String.class, Object.class);
            //获取会话密钥（session_key）
            String sessionKey = map.get("session_key").toString();
            //对data进行AES解密
//            String result = decrypt(authorizeVO.getData(), sessionKey, authorizeVO.getIv());
            String result = AesCbcUtil.decrypt(authorizeVO.getData(), sessionKey, authorizeVO.getIv(), "UTF-8");

            if (!StringUtils.isEmpty(result)) {
                Map<String, Object> userInfoMap = JsonUtils.json2object(result, Map.class, String.class, Object.class);

                logger.info(" userInfoMap {}", userInfoMap);
                userInfo = new WeChatUser();
                userInfo.setOpenId(userInfoMap.get("openId").toString());
                userInfo.setNickname(userInfoMap.get("nickName").toString());
                userInfo.setGender(Integer.parseInt(userInfoMap.get("gender").toString()));
                userInfo.setCity(userInfoMap.get("city").toString());
                userInfo.setProvince(userInfoMap.get("province").toString());
//                userInfo.setUnionId(userInfoMap.get("unionId").toString());
                userInfo.setAvatar(userInfoMap.get("avatarUrl").toString());
            }
        } catch (Exception e) {
            logger.error(" authorize get user info error {}", e);
        }
        logger.info(" userInfo is {} ", userInfo);
        return userInfo;
    }

    /**
     * AES解密
     *
     * @param data 密文
     * @param key  秘钥
     * @param iv   偏移量
     * @return
     */
    private String decrypt(String data, String key, String iv) {
        //待加密数据,加密秘钥,偏移量
        byte[] dataByte = Base64Utils.decode(data.getBytes()), keyByte = Base64Utils.decode(key.getBytes()),
                ivByte = Base64Utils.decode(iv.getBytes());
        try {
//            Cipher.getInstance("AES/DES/CBC/PKCS5Padding");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);

            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                logger.error(" result is {} ", result);
                return result;
            }
        } catch (Exception e) {
            logger.error(" decrypt error {} ", e);
        }
        return null;
    }


    /**
     * 企业付款
     *
     * @param
     * @return
     * @throws Exception
     */
    public Boolean businessPayment(String openId, BigDecimal money, String desc) throws Exception {

        //将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序）
        SortedMap<String, String> packageParams = new TreeMap<>();
        packageParams.put("mch_appid", appId);
        packageParams.put("mchid", mchId);
        packageParams.put("nonce_str", getNonceStr());
        packageParams.put("partner_trade_no", getTradeNo());
        packageParams.put("openid", openId);
        packageParams.put("check_name", "NO_CHECK");
        packageParams.put("re_user_name", "");
        packageParams.put("amount", money.toString());// TODO: 2018-08-07 参数
        packageParams.put("desc", desc);
        packageParams.put("spbill_create_ip", "127.0.0.1");

        //使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA
        String stringA = mapMontageAsString(packageParams);

        //在stringA最后拼接上key得到stringSignTemp字符串
        String stringSignTemp = stringA + "&key=" + mchKey;

        //对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写
        String paySign = MD5Digest(stringSignTemp).toUpperCase();


        String xml = "<xml>"
                + "<mch_appid>" + packageParams.get("mch_appid") + "</mch_appid>"
                + "<mchid>" + packageParams.get("mchid") + "</mchid>"
                + "<nonce_str>" + packageParams.get("nonce_str") + "</nonce_str>"
                + "<partner_trade_no>" + packageParams.get("partner_trade_no") + "</partner_trade_no>"
                + "<openid>" + packageParams.get("openid") + "</openid>"
                + "<check_name>" + packageParams.get("check_name") + "</check_name>"
                + "<re_user_name>" + packageParams.get("re_user_name") + "</re_user_name>"
                + "<amount>" + packageParams.get("amount") + "</amount>"
                + "<desc>" + packageParams.get("desc") + "</desc>"
                + "<spbill_create_ip>" + packageParams.get("spbill_create_ip") + "</spbill_create_ip>"
                + "<sign>" + paySign + "</sign>"
                + "</xml>";

        logger.info("------xml:{}------", xml);
        String response = ssl.ssl("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers", xml);
        Map<String, String> map = xmlUtil.parseXml(response);
        logger.info("--------map:{}--------", map);

        if (map.get("return_code").equals("SUCCESS") && map.get("result_code").equals("SUCCESS")) {
            logger.debug(packageParams.get("openid") + "====" + packageParams.get("amount") + "=====企业付款成功");
            return true;
        } else {
            logger.debug(packageParams.get("openid") + "====" + packageParams.get("amount") + "=====企业付款失败");
            return false;

        }
    }

    /**
     * 获取随机字符串
     *
     * @return
     */
    public static String getNonceStr() {
        // 随机数
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currTime = outFormat.format(now);

        // 8位日期
        String strTime = currTime.substring(8, currTime.length());

        // 四位随机数
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < 4; i++) {
            num = num * 10;
        }
        String strRandom = (int) ((random * num)) + "";

        // 10位序列号,可以自行调整。
        return strTime + strRandom;
    }

    /**
     * 生成订单号
     *
     * @return
     */
    public static String getTradeNo() {

        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = Md5Encrypt.md5(UUID.randomUUID().toString()).hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        String tradeNo = machineId + String.format("%015d", hashCodeV);
        System.out.println(tradeNo);
        return tradeNo;
    }

    /**
     * 已经经过字典序排序的map拼接为一个字符串key1=value1&key2=value2&key3=value3
     */
    public static String mapMontageAsString(SortedMap<String, String> sortedMap) {
        StringBuffer sb = new StringBuffer();
        Set es = sortedMap.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (v != null && !v.equals("")) {
                sb.append(k + "=" + v + "&");
            }
        }
        return sb.substring(0, sb.lastIndexOf("&"));
    }

    /**
     * MD5加密
     */
    public static String MD5Digest(String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 将拼接而成的字符串进行MD5加密
            byte[] digest = md.digest(content.getBytes("utf-8"));
            //将加密后的字节数组转成十六进制的字符串
            return byteArrayToHexStr(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 字节数组转换为十六进制的字符串
     *
     * @param byteArray
     * @return
     */
    public static String byteArrayToHexStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 字节转换为十六进制的字符串
     *
     * @param mByte
     * @return
     */
    public static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }
}

