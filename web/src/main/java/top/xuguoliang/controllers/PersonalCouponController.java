package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
