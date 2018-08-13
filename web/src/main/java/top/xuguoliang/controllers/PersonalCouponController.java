package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.coupon.PersonalCouponWebService;
import top.xuguoliang.service.coupon.web.PersonalCouponWebResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/personalCoupon")
@Api(tags = "个人卡券")
public class PersonalCouponController {

    @Resource
    private PersonalCouponWebService personalCouponWebService;

    @GetMapping
    @ApiOperation("个人卡券列表")
    public PersonalCouponWebResultVO findAll() {
        Integer userId = UserHelper.getUserId();
        return personalCouponWebService.findAll(userId);
    }

    @DeleteMapping("/{personalCouponId}")
    public void deletePersonalCoupon(@PathVariable Integer personalCouponId) {
        Integer userId = UserHelper.getUserId();
        personalCouponWebService.deletePersonalCoupon(userId, personalCouponId);
    }
}
