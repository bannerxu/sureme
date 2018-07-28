package top.xuguoliang.models.commodity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface CommodityDao extends JpaSpecificationExecutor<Commodity>, JpaRepository<Commodity, Integer> {
}
