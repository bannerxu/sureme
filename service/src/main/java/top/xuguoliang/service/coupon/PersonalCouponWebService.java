package top.xuguoliang.service.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.models.coupon.PersonalCoupon;
import top.xuguoliang.models.coupon.PersonalCouponDao;
import top.xuguoliang.service.coupon.web.PersonalCouponWebResultVO;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Service
public class PersonalCouponWebService {

    @Resource
    private PersonalCouponDao personalCouponDao;

    /**
     * 查询指定用户的个人卡券
     *
     * @param userId 用户id
     * @return 个人卡券
     */
    public Page<PersonalCouponWebResultVO> findAll(Integer userId, Pageable pageable) {
        return personalCouponDao.findByUserIdIsAndDeletedIsFalse(userId, pageable)
                .map(personalCoupon -> {
                    Date date = new Date();
                    if (!ObjectUtils.isEmpty(personalCoupon)) {
                        PersonalCouponWebResultVO vo = personalCouponToVO(personalCoupon);
                        if (vo.getUseBeginTime().before(date) && vo.getUseEndTime().after(date)) {
                            vo.setValid(true);
                        } else {
                            vo.setValid(false);
                        }
                        return vo;
                    } else {
                        return null;
                    }
                });
    }

    private PersonalCouponWebResultVO personalCouponToVO(PersonalCoupon personalCoupon) {
        PersonalCouponWebResultVO vo = new PersonalCouponWebResultVO();
        BeanUtils.copyNonNullProperties(personalCoupon, vo);
        return vo;
    }

    /**
     * 删除指定的个人卡券
     *
     * @param userId           用户id
     * @param personalCouponId 个人卡券
     */
    public void deletePersonalCoupon(Integer userId, Integer personalCouponId) {
        PersonalCoupon personalCoupon = personalCouponDao.findByPersonalCouponIdIsAndUserIdIsAndDeletedIsFalse(personalCouponId, userId);
        if (!ObjectUtils.isEmpty(personalCoupon)) {
            personalCoupon.setDeleted(true);
            personalCouponDao.saveAndFlush(personalCoupon);
        }
    }
}
