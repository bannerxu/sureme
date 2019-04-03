package top.banner.common.utils;


/**
 * @author jinguoguo
 */
public class NumberUtil {

    /**
     * 生成订单编号：前缀（两个字母）+ 时间戳 + 4 位随机数
     *
     * @param prefix 前缀
     * @return 订单编号
     */
    public static String generateOrderNumber(String prefix) {
        String time = String.valueOf(System.currentTimeMillis());
        int number = (int) ((Math.random() * 9 + 1) * 1000);
        return prefix + time + number;
    }
}
