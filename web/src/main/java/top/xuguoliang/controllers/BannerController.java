package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xuguoliang.service.banner.BannerWebService;
import top.xuguoliang.service.banner.web.BannerWebResultVO;

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

    @GetMapping
    @ApiOperation("查询所有")
    public List<BannerWebResultVO> findAll() {
        return bannerWebService.findAll();
    }

}
