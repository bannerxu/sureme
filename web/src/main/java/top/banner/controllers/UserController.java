package top.banner.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.common.utils.WeChatUtil;
import top.banner.models.user.User;
import top.banner.service.user.UserWebService;
import top.banner.service.user.web.ArticleStarResultVO;
import top.banner.service.user.web.AuthorizeVO;
import top.banner.service.user.web.UserSetPregnancyVO;

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

    @PutMapping("setPregnancyType")
    @ApiOperation("选择孕期")
    public Boolean setPregnancyType(@RequestBody UserSetPregnancyVO userSetPregnancyVO) {
        Integer userId = UserHelper.getUserId();
        return userWebService.setPregnancyType(userId, userSetPregnancyVO);
    }

    @GetMapping("star")
    @ApiOperation("查看收藏")
    public Page<ArticleStarResultVO> findStar(@PageableDefault Pageable pageable) {
        Integer userId = UserHelper.getUserId();
        return userWebService.findStar(userId, pageable);
    }

    @DeleteMapping("star/{articleStarId}")
    @ApiOperation("删除收藏")
    public void deleteStar(@PathVariable Integer articleStarId) {
        Integer userId = UserHelper.getUserId();
        userWebService.deleteStar(userId, articleStarId);
    }

    @GetMapping
    @ApiOperation("个人信息")
    public User getUserInfo() {
        Integer userId = UserHelper.getUserId();
        return userWebService.getUser(userId);
    }

}


