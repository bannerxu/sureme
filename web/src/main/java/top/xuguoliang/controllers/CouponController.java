package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.coupon.CouponWebService;
import top.xuguoliang.service.coupon.web.CouponWebResultVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/coupon")
@Api(tags = "卡券模块")
public class CouponController {

    @Resource
    private CouponWebService couponWebService;

    @GetMapping("commodity/{commodityId}")
    @ApiOperation("查询指定商品的所有优惠券")
    public List<CouponWebResultVO> findAllByCommodityId(@PathVariable Integer commodityId) {
        return couponWebService.findAllByCommodityId(commodityId);
    }

    @PostMapping("/{couponId}")
    @ApiOperation("领取优惠券")
    public Boolean pullCoupon(@PathVariable Integer couponId) {
        Integer userId = UserHelper.getUserId();
        return couponWebService.pullCoupon(userId, couponId);
    }
}
