package top.xuguoliang.models.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface OrderDao extends JpaSpecificationExecutor<Order>, JpaRepository<Order, Integer> {
}
