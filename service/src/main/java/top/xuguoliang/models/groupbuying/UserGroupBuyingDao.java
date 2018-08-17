package top.xuguoliang.models.groupbuying;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface UserGroupBuyingDao extends JpaSpecificationExecutor<UserGroupBuying>, JpaRepository<UserGroupBuying, Integer> {
    /**
     * 分页查询人数已满的用户拼团
     * @param pageable 分页信息
     * @return 用户拼团
     */
    Page<UserGroupBuying> findByIsFullIsTrue(Pageable pageable);
}
