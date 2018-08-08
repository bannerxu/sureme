package top.xuguoliang.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.common.utils.WeChatUtil;
import top.xuguoliang.models.user.User;
import top.xuguoliang.service.user.UserWebService;
import top.xuguoliang.service.user.web.AuthorizeVO;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * @author jinguoguo
 */
@RestController
@Api(tags = "用户模块")
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private WeChatUtil weChatUtil;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserWebService userWebService;

    @ApiOperation("授权")
    @PostMapping("authorize")
    public User authorize(@RequestBody @Valid AuthorizeVO authorizeVO) {
        logger.debug("调用接口：用户授权：{}", authorizeVO);
        return userWebService.authorize(authorizeVO);
    }

    @ApiOperation("获取小程序")
    @GetMapping("getAccessToken")
    public String getAccessToken() {
        return weChatUtil.getAccessToken();
    }
}


