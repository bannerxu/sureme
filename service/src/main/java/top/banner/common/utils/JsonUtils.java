package top.banner.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Json工具类，实现实体类和Json数据格式之间的互转功能
 */
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * jackson包提供的json与javabean的转换类ObjectMapper
     */
    private static ObjectMapper objectMapper;

    static {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    /**
     * 将javabean对象转换为json对象的字符串
     *
     * @param obj 要转换的javabean对象
     * @return String类型--json对象的字符串
     */
    public static String object2json(Object obj) {
        // 变量json--返回用的json对象的字符串
        String json = null;
        if (obj != null) {
            try {
                json = objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                logger.error(" JsonUtils object2json() error ", e);
            }
        }
        return json;
    }

    /**
     * 将json对象的字符串转换为javabean对象
     *
     * @param json            要转换的json对象的字符串
     * @param collectionClass 原始类的类对象
     * @param elementClasses  泛型类的类对象
     * @param <T>             T类型--期望的javabean对象(T类型)，调用后需要结合具体类型进行强制转换
     * @return
     */
    static <T> T json2object(String json, Class<?> collectionClass, Class<?>... elementClasses) {
        T t = null;
        if (json != null) {
            // 变量object--返回用的javabean对象
            JavaType javaType = getCollectionType(collectionClass, elementClasses);
            try {
                t = objectMapper.readValue(json, javaType);
            } catch (IOException e) {
                logger.error(" JsonUtils json2object() error ", e);
            }
        }
        return t;
    }

    /**
     * 将原始类的类对象与泛型类的类对象通过objectMapper绑定为jackson包提供的JavaType对象
     *
     * @param collectionClass 原始类的类对象
     * @param elementClasses  泛型类的类对象
     * @return JavaType类型--绑定后的JavaType对象
     */
    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}