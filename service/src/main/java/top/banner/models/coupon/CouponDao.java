package top.banner.models.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
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

    /**
     * 分页查询在领取时间内的卡券
     * @param pullBeginTime 当前时间
     * @param pullEndTime 当前时间
     * @param pageable 分页信息
     * @return 分页卡券
     */
    Page<Coupon> findByPullBeginTimeBeforeAndPullEndTimeAfterAndDeletedIsFalse(Date pullBeginTime, Date pullEndTime, Pageable pageable);

}
