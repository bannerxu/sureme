package top.xuguoliang.models.second;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

/**
 * @author jinguoguo
 */
public interface SecondDao extends JpaSpecificationExecutor<Second>, JpaRepository<Second, Integer> {
    /**
     * 分页查询没到结束时间的未删除的秒杀
     * @param endTime 结束时间
     * @return 秒杀
     */
    Page<Second> findAllByEndTimeAfterAndDeletedIsFalse(Date endTime, Pageable pageable);
}
