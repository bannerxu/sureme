package top.xuguoliang.service.coupon;

import org.springframework.stereotype.Service;
import top.xuguoliang.models.coupon.PersonalCouponDao;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class PersonalCouponWebService {

    @Resource
    private PersonalCouponDao personalCouponDao;

}
