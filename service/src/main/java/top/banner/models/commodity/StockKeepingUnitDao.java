package top.banner.models.commodity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface StockKeepingUnitDao extends JpaSpecificationExecutor<StockKeepingUnit>, JpaRepository<StockKeepingUnit, Integer> {
    /**
     * 通过商品id查询未删除的规格
     *
     * @param commodityId 商品id
     * @return 商品规格
     */
    List<StockKeepingUnit> findByCommodityIdIsAndDeletedIsFalse(Integer commodityId);

    /**
     * 通过商品id查询规格，忽略deleted
     *
     * @param commodityId 商品id
     * @return 商品规格
     */
    List<StockKeepingUnit> findByCommodityIdIs(Integer commodityId);
}
