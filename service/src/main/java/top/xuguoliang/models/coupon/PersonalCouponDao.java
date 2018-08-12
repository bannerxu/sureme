package top.xuguoliang.models.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface PersonalCouponDao extends JpaSpecificationExecutor<PersonalCoupon>, JpaRepository<PersonalCoupon, Integer> {
    /**
     * 通过用户id和卡券id查找个人卡券
     *
     * @param userId   用户id
     * @param couponId 卡券id
     * @return 个人卡券
     */
    PersonalCoupon findByUserIdIsAndCouponIdIsAndDeletedIsFalse(Integer userId, Integer couponId);

    /**
     * 查找指定用户的未删除的个人卡券
     *
     * @param userId 用户id
     * @return 个人卡券
     */
    PersonalCoupon findByUserIdIsAndDeletedIsFalse(Integer userId);
}
