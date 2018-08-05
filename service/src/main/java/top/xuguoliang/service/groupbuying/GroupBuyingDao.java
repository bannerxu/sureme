package top.xuguoliang.service.groupbuying;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.xuguoliang.models.groupbuying.GroupBuying;

/**
 * @author jinguoguo
 */
public interface GroupBuyingDao extends JpaSpecificationExecutor<GroupBuying>, JpaRepository<GroupBuying, Integer> {
}
