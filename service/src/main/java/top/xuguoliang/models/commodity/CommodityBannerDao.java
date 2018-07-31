package top.xuguoliang.models.commodity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface CommodityBannerDao extends JpaSpecificationExecutor<CommodityBanner>, JpaRepository<CommodityBanner, Integer> {
    /**
     * 根据商品id查找对应的商品轮播
     * @param commodityId 商品id
     * @return 商品轮播List
     */
    List<CommodityBanner> findByCommodityIdIsAndDeletedIsFalse(Integer commodityId);
    List<CommodityBanner> findByCommodityIdIs(Integer commodityId);
}
