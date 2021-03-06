package top.banner.models.groupbuying;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

/**
 * @author jinguoguo
 */
public interface UserGroupBuyingDao extends JpaSpecificationExecutor<UserGroupBuying>, JpaRepository<UserGroupBuying, Integer> {
    /**
     * 分页查询人数已满的用户拼团
     *
     * @param pageable 分页信息
     * @return 用户拼团
     */
    Page<UserGroupBuying> findByIsFullIsTrue(Pageable pageable);

    /**
     * 分页查询在时间内的人数未满的用户拼团且按创建时间倒序
     *
     * @param pageable 分页信息
     * @return 用户拼团
     */
    Page<UserGroupBuying> findByBeginTimeBeforeAndEndTimeAfterAndIsFullIsFalseOrderByCreateTimeDesc(Date beginTime,
                                                                                                    Date endTime,
                                                                                                    Pageable pageable);
}
