package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.banner.models.banner.BannerTypeEnum;
import top.banner.service.banner.BannerWebService;
import top.banner.service.banner.web.BannerWebResultVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/banner")
@Api(tags = "轮播")
public class BannerController {

    @Resource
    private BannerWebService bannerWebService;

    @GetMapping("/{bannerType}")
    @ApiOperation("查询所有")
    public List<BannerWebResultVO> findAll(@PathVariable BannerTypeEnum bannerType) {
        return bannerWebService.findAll(bannerType);
    }

}
