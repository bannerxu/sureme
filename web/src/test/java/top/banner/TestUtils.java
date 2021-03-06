package top.banner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.banner.common.utils.WeChatUtil;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SwaggerApplication.class)
public class TestUtils {

    @Resource
    private WeChatUtil weChatUtil;

    @Test
    public void test1() {
        System.out.println(weChatUtil.getQRCode(430,"index","1/2"));
    }
}
