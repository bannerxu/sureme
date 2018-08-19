package top.xuguoliang.models.brokerage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface BrokerageDao extends JpaRepository<Brokerage, Integer> {
    /**
     * 通过用户id分页查找
     *
     * @param userId   用户id
     * @param pageable 分页信息
     * @return 分页实体
     */
    Page<Brokerage> findAllByUserId(Integer userId, Pageable pageable);

    /**
     * 通过用户id查找
     *
     * @param userId
     * @return
     */
    List<Brokerage> findByUserId(Integer userId);
}
