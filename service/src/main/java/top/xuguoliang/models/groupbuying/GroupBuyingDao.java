package top.xuguoliang.models.groupbuying;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.xuguoliang.models.groupbuying.GroupBuying;

import java.util.Date;

/**
 * @author jinguoguo
 */
public interface GroupBuyingDao extends JpaSpecificationExecutor<GroupBuying>, JpaRepository<GroupBuying, Integer> {
    Page<GroupBuying> findAllByBeginTimeBeforeAndEndTimeAfterAndDeletedIsFalseOrderByCreateTimeDesc(Date beginTime,
                                                                                                    Date endTime,
                                                                                                    Pageable pageable);
}
