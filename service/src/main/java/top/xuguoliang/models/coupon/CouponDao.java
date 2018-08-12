package top.xuguoliang.models.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface CouponDao extends JpaSpecificationExecutor<Coupon>, JpaRepository<Coupon, Integer> {
    /**
     * 查询未删除的卡券
     *
     * @return 未删除的卡券
     */
    List<Coupon> findByDeletedIsFalse();
}
