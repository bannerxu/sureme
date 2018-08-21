package top.xuguoliang.models.moneywater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface MoneyWaterDao extends JpaSpecificationExecutor<MoneyWater>, JpaRepository<MoneyWater, Integer> {
}
