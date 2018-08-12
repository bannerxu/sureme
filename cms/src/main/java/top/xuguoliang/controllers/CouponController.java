package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.xuguoliang.common.constants.RoleConstant;
import top.xuguoliang.service.coupon.CouponCmsService;
import top.xuguoliang.service.coupon.cms.CouponCmsAddVO;
import top.xuguoliang.service.coupon.cms.CouponCmsResultVO;
import top.xuguoliang.service.coupon.cms.CouponCmsUpdateVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/coupon")
@Api(tags = "卡券模块")
public class CouponController {

    @Resource
    private CouponCmsService couponCmsService;

    @GetMapping("page")
    @ApiOperation("分页查询")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public Page<CouponCmsResultVO> findPage(@PageableDefault Pageable pageable) {
        return couponCmsService.findPage(pageable);
    }

    @GetMapping("/{couponId}")
    @ApiOperation("单个查询")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public CouponCmsResultVO getCoupon(@PathVariable Integer couponId) {
        return couponCmsService.getCoupon(couponId);
    }

    @PostMapping
    @ApiOperation("添加")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public CouponCmsResultVO addCoupon(@RequestBody CouponCmsAddVO couponCmsAddVO) {
        return couponCmsService.addCoupon(couponCmsAddVO);
    }

    @DeleteMapping("/{couponId}")
    @ApiOperation("删除")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public void deleteCoupon(@PathVariable Integer couponId) {
        couponCmsService.deleteCoupon(couponId);
    }

    @PutMapping
    @ApiOperation("修改")
    @ApiIgnore
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public CouponCmsResultVO updateCoupon(@RequestBody CouponCmsUpdateVO couponCmsUpdateVO) {
        return couponCmsService.updateCoupon(couponCmsUpdateVO);
    }
}
