package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.comment.web.CommodityCommentWebResultVO;
import top.xuguoliang.service.commodity.CommodityWebService;
import top.xuguoliang.service.commodity.web.CommodityWebDetailVO;
import top.xuguoliang.service.commodity.web.CommodityWebResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/commodity")
@Api(tags = "商品模块")
public class CommodityController {

    @Resource
    private CommodityWebService commodityWebService;

    @GetMapping
    @ApiOperation("分页，分类id可不传")
    public Page<CommodityWebResultVO> findPage(@RequestParam(required = false) Integer categoryId,
                                               @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return commodityWebService.findPage(categoryId, pageable);
    }

    @GetMapping("/{commodityId}")
    @ApiOperation("商品详情")
    public CommodityWebDetailVO getCommodityDetail(@PathVariable Integer commodityId) {
        Integer userId = UserHelper.getUserId();
        return commodityWebService.getCommodityDetail(userId, commodityId);
    }

    @GetMapping("comment/{commodityId}")
    public Page<CommodityCommentWebResultVO> findPageComment(@PathVariable Integer commodityId,
                                                             @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return commodityWebService.findPageComment(commodityId, pageable);
    }

    @GetMapping("/share")
    @ApiOperation("生成分享图片")
    public String generateShareImage(@ApiParam("大小") @RequestParam Integer width,
                                     @ApiParam("跳转地址") @RequestParam String path,
                                     @ApiParam("业务参数，不可以包含中文") @RequestParam String scene,
                                     @ApiParam("商品id") @RequestParam Integer commodityId) {

        return commodityWebService.generateShareImage(width, path, scene, commodityId);
    }

}
