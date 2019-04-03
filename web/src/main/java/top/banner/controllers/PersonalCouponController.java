package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.service.coupon.PersonalCouponWebService;
import top.banner.service.coupon.web.PersonalCouponWebResultVO;

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
    public Page<PersonalCouponWebResultVO> findAll(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Integer userId = UserHelper.getUserId();
        return personalCouponWebService.findAll(userId, pageable);
    }

    @DeleteMapping("/{personalCouponId}")
    @ApiOperation("删除")
    public void deletePersonalCoupon(@PathVariable Integer personalCouponId) {
        Integer userId = UserHelper.getUserId();
        personalCouponWebService.deletePersonalCoupon(userId, personalCouponId);
    }
}
