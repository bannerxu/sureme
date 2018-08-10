package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
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
                                               @PageableDefault Pageable pageable) {
        return commodityWebService.findPage(categoryId, pageable);
    }

    @GetMapping("/{commodityId}")
    @ApiOperation("商品详情")
    public CommodityWebDetailVO getCommodityDetail(@PathVariable Integer commodityId) {
        Integer userId = UserHelper.getUserId();
        return commodityWebService.getCommodityDetail(userId, commodityId);
    }

}
