package top.banner.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.models.shareitem.ShareItem;
import top.banner.service.shareitem.ShareItemWebService;
import top.banner.service.shareitem.web.ShareItemAddVO;
import top.banner.service.shareitem.web.ShareItemVO;

import javax.annotation.Resource;


/**
 * @author jinguoguo
 */
@RestController
@Api(tags = "分销")
@RequestMapping("/api/shareItem")
public class ShareItemController {

    @Resource
    private ShareItemWebService shareItemWebService;

    @ApiOperation("分享，生成下线")
    @PostMapping
    public ShareItem share(@RequestBody ShareItemAddVO addVO) {
        return shareItemWebService.addShareItem(addVO);
    }

    @ApiOperation("我的下线")
    @GetMapping("/{type}")
    public Page<ShareItemVO> findShareItemPage(@ApiParam("类型 1 - 一级 2 - 二级") @PathVariable Integer type,
                                               @PageableDefault Pageable pageable) {
        return shareItemWebService.findShareItemPage(UserHelper.getUserId(), type, pageable);

    }
}


