package top.banner.models.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * 分页查找指定用户的未删除的个人卡券
     *
     * @param pageable 分页信息
     * @param userId 用户id
     * @return 个人卡券
     */
    Page<PersonalCoupon> findByUserIdIsAndDeletedIsFalse(Integer userId, Pageable pageable);

    /**
     * 查找指定用户的
     * @param personalCouponId 主键id
     * @param userId 用户id
     * @return 个人卡券
     */
    PersonalCoupon findByPersonalCouponIdIsAndUserIdIsAndDeletedIsFalse(Integer personalCouponId, Integer userId);
}
