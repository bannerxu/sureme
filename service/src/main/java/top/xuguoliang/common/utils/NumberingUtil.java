package top.xuguoliang.common.utils;

import org.springframework.stereotype.Component;

@Component
public class NumberingUtil {

    public String getWithdrawCode(Integer userId) {
        return "TX-" + userId + "-" + ((System.currentTimeMillis()+""));
    }

}
