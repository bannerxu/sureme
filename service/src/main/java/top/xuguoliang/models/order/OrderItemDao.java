package top.xuguoliang.models.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface OrderItemDao extends JpaSpecificationExecutor<OrderItem>, JpaRepository<OrderItem, Integer> {
}
