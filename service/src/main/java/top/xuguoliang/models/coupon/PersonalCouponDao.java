package top.xuguoliang.models.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface PersonalCouponDao extends JpaSpecificationExecutor<PersonalCoupon>, JpaRepository<PersonalCoupon, Integer> {
}
