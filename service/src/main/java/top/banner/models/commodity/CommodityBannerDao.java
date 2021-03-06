package top.banner.models.commodity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface CommodityBannerDao extends JpaSpecificationExecutor<CommodityBanner>, JpaRepository<CommodityBanner, Integer> {
    /**
     * 根据商品id查找未删除的商品轮播
     *
     * @param commodityId 商品id
     * @return 商品轮播List
     */
    List<CommodityBanner> findByCommodityIdIsAndDeletedIsFalse(Integer commodityId);

    List<CommodityBanner> findByCommodityIdIs(Integer commodityId);

    /**
     * 查询商品轮播，通过id排序
     *
     * @param commodityId 商品id
     * @return 商品轮播列表
     */
    List<CommodityBanner> findByCommodityIdIsAndDeletedIsFalseOrderByCommodityBannerIdAsc(Integer commodityId);

    /**
     * 查询指定商品id的最早创建的轮播图
     *
     * @param commodityId 商品id
     * @return 轮播
     */
    CommodityBanner findFirstByCommodityIdIsAndDeletedIsFalseOrderByCreateTimeAsc(Integer commodityId);
}
