package top.xuguoliang.models.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface OrderItemDao extends JpaSpecificationExecutor<OrderItem>, JpaRepository<OrderItem, Integer> {
    /**
     * 通过订单id查询所有订单条目
     * @param orderId 订单id
     * @return 订单条目列表
     */
    List<OrderItem> findByOrderIdIs(Integer orderId);
}
