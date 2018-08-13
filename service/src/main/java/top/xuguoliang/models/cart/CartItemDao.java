package top.xuguoliang.models.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface CartItemDao extends JpaSpecificationExecutor<CartItem>, JpaRepository<CartItem, Integer> {
    List<CartItem> findByUserIdIsAndDeletedIsFalse(Integer userId);

    CartItem findByUserIdIsAndStockKeepingUnitIdIsAndDeletedIsFalse(Integer userId, Integer stockKeepingUnitId);

    CartItem findByUserIdIsAndCartItemIdIsAndDeletedIsFalse(Integer userId, Integer cartItemId);

    List<CartItem> findByUserIdIsAndDeletedIsFalseOrderByUpdateTimeDesc(Integer userId);
}
