package top.xuguoliang.models.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface RelationCouponCommodityDao extends JpaSpecificationExecutor<RelationCouponCommodity>, JpaRepository<RelationCouponCommodity, Integer> {
    /**
     * 通过卡券id查询所有关系
     *
     * @param couponId 卡券id
     * @return 卡券与商品关系列表
     */
    List<RelationCouponCommodity> findByCouponIdIs(Integer couponId);

    /**
     * 通过商品id查询所有关系
     * @param commodityId 商品id
     * @return 卡券与商品的关系列表
     */
    List<RelationCouponCommodity> findByCommodityIdIs(Integer commodityId);

    /**
     * 通过卡券id和商品id查询关系
     * @param couponId 卡券id
     * @param commodityId 商品id
     * @return 卡券与商品的关系（单个）
     */
    RelationCouponCommodity findByCouponIdIsAndCommodityIdIs(Integer couponId, Integer commodityId);
}
