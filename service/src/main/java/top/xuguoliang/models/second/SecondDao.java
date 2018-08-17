package top.xuguoliang.models.second;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface SecondDao extends JpaSpecificationExecutor<Second>, JpaRepository<Second, Integer> {
}
