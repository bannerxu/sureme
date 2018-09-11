package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.common.constants.RoleConstant;
import top.xuguoliang.service.commodity.CommodityCmsService;
import top.xuguoliang.service.commodity.cms.CommodityCmsAddParamVO;
import top.xuguoliang.service.commodity.cms.CommodityCmsResultVO;
import top.xuguoliang.service.commodity.cms.CommodityCmsUpdateParamVO;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/commodity")
@Api(tags = "商品模块")
public class CommodityController {

    @Resource
    private CommodityCmsService commodityCmsService;

    @GetMapping("page")
    @ApiOperation("分页查询")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public Page<CommodityCmsResultVO> page(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return commodityCmsService.findPage(pageable);
    }

    @GetMapping("/{commodityId}")
    @ApiOperation("查询商品")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public CommodityCmsResultVO getCommodity(@PathVariable @NotNull(message = "商品id不能为空") Integer commodityId) {
        return commodityCmsService.getCommodity(commodityId);
    }

    @PostMapping
    @ApiOperation("添加")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public CommodityCmsResultVO add(@RequestBody CommodityCmsAddParamVO commodityCmsAddParamVO) {
        return commodityCmsService.addCommodity(commodityCmsAddParamVO);
    }

    @DeleteMapping("/{commodityId}")
    @ApiOperation("删除")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public void delete(@PathVariable Integer commodityId) {
        commodityCmsService.deleteCommodity(commodityId);
    }

    @PutMapping
    @ApiOperation("修改")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public CommodityCmsResultVO update(@RequestBody CommodityCmsUpdateParamVO commodityCmsUpdateParamVO) {
        return commodityCmsService.update(commodityCmsUpdateParamVO);
    }

    @DeleteMapping("banner/{commodityBannerId}")
    @ApiModelProperty("删除轮播")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public boolean deleteCommodityBanner(@PathVariable @NotNull Integer commodityBannerId) {
        return commodityCmsService.deleteCommodityBanner(commodityBannerId);
    }

    @DeleteMapping("sku/{skuId}")
    @ApiModelProperty("删除规格")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public boolean deleteSKU(@PathVariable @NotNull Integer skuId) {
        return commodityCmsService.deleteSKU(skuId);
    }

}
