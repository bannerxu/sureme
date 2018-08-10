package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
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

    public PersonalCouponWebResultVO findAll() {

        return null;
    }

}
