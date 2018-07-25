package top.xuguoliang.controllers;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.xuguoliang.models.manager.Manager;
import top.xuguoliang.service.RedisKeyPrefix;
import top.xuguoliang.service.manager.LoginService;
import top.xuguoliang.service.manager.cms.ManagerLoginVO;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author jinguoguo
 */
@RestController
@Api(tags = "登录模块")
@RequestMapping("api/login")
public class LoginController {

    @Resource
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private LoginService loginService;

    @Resource
    private DefaultKaptcha captchaProducer;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("captcha")
    @ApiOperation("验证码")
    @ApiIgnore
    public void getCaptcha(@RequestParam @ApiParam("uuid") String uuid, HttpServletResponse res) {
        String key = RedisKeyPrefix.captcha(uuid);
        logger.info(" login uuid is {}", uuid);
        ValueOperations<String, String> redis = stringRedisTemplate.opsForValue();

        String capText = redis.get(key);
        if (StringUtils.isEmpty(capText)) {
            //生成验证码
            capText = captchaProducer.createText();
            logger.info("----------capText:" + capText + "----------------");
            //放入redis中，寿命为5分钟
            redis.set(RedisKeyPrefix.captcha(uuid), capText, 5, TimeUnit.MINUTES);
        }

        res.setDateHeader("Expires", 0);
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        res.setHeader("Cache-Control", "post-check=0, pre-check=0");
        res.setHeader("Pragma", "no-cache");
        res.setContentType("image/jpeg");
        ServletOutputStream out = null;
        try {
            out = res.getOutputStream();
            BufferedImage bi = captchaProducer.createImage(capText);
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (IOException e) {
            logger.info("----------验证码输出错误--------", e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.info("close output stream error", e);
                }
            }
        }
    }

    @ApiOperation("登录")
    @PostMapping
    public Manager login(@RequestBody @Valid ManagerLoginVO loginVO) {
        logger.info(" login VO : {}", loginVO);
        return loginService.login(loginVO);
    }

}
