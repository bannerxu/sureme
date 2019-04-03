package top.banner.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.banner.models.user.User;
import top.banner.service.user.UserWebService;

import javax.annotation.Resource;


/**
 * @author jinguoguo
 */
@RestController
@Api(tags = "后台测试专用")
@RequestMapping("/api/test")
public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource
    private UserWebService userWebService;

    @ApiOperation("授权")
    @PostMapping("login")
    public User login() {
        logger.debug("调用接口：后台测试授权：{}");
        return userWebService.authorize();
    }

}


