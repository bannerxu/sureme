package top.xuguoliang.models.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface OrderDao extends JpaSpecificationExecutor<Order>, JpaRepository<Order, Integer> {
    /**
     * 通过订单号码查询订单
     * @param orderNumber 订单号码
     * @return 订单
     */
    Order findByOrderNumberEquals(String orderNumber);
}
