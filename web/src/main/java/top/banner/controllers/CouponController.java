package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.service.coupon.CouponWebService;
import top.banner.service.coupon.web.CouponWebResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/coupon")
@Api(tags = "卡券模块")
public class CouponController {

    @Resource
    private CouponWebService couponWebService;

    @GetMapping
    @ApiOperation("查询所有优惠券")
    public Page<CouponWebResultVO> findAll(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Integer userId = UserHelper.getUserId();
        return couponWebService.findPage(userId, pageable);
    }

    @PostMapping("/{couponId}")
    @ApiOperation("领取优惠券")
    public Boolean pullCoupon(@PathVariable Integer couponId) {
        Integer userId = UserHelper.getUserId();
        return couponWebService.pullCoupon(userId, couponId);
    }
}
