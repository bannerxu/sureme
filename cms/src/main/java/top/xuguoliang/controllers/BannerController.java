package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.models.banner.BannerTypeEnum;
import top.xuguoliang.service.banner.BannerCmsService;
import top.xuguoliang.service.banner.cms.BannerCmsAddParamVO;
import top.xuguoliang.service.banner.cms.BannerCmsResultVO;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/banner")
@Api(tags = "轮播模块")
public class BannerController {

    @Resource
    private BannerCmsService bannerCmsService;

    @GetMapping
    @ApiOperation("查询指定类型的轮播图")
    public List<BannerCmsResultVO> getBanners(@ApiParam(name = "轮播类型") @NotNull @RequestParam BannerTypeEnum bannerType) {
        return bannerCmsService.getBanners(bannerType);
    }

    @PostMapping
    @ApiOperation("添加轮播")
    public BannerCmsResultVO addBanner(@RequestBody BannerCmsAddParamVO bannerCmsAddParamVO) {
        return bannerCmsService.addBanner(bannerCmsAddParamVO);
    }

    @DeleteMapping("/{bannerId}")
    @ApiOperation("删除轮播")
    public void deleteBanner(@NotNull @PathVariable Integer bannerId) {
        bannerCmsService.deleteBanner(bannerId);
    }

}
