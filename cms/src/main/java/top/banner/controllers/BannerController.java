package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.banner.models.banner.BannerTypeEnum;
import top.banner.service.banner.BannerCmsService;
import top.banner.service.banner.cms.BannerCmsAddParamVO;
import top.banner.service.banner.cms.BannerCmsResultVO;

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
    public List<BannerCmsResultVO> getBanners(@NotNull @RequestParam BannerTypeEnum bannerType) {
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
