package top.xuguoliang.service.coupon;

import org.springframework.stereotype.Service;
import top.xuguoliang.models.coupon.PersonalCoupon;
import top.xuguoliang.models.coupon.PersonalCouponDao;
import top.xuguoliang.service.coupon.web.PersonalCouponWebResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class PersonalCouponWebService {

    @Resource
    private PersonalCouponDao personalCouponDao;

    public PersonalCouponWebResultVO findAll(Integer userId) {
        PersonalCoupon personalCoupon = personalCouponDao.findByUserIdIsAndDeletedIsFalse(userId);
        return null;
    }
}
