package top.xuguoliang.models.commodity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface CommodityDao extends JpaSpecificationExecutor<Commodity>, JpaRepository<Commodity, Integer> {
    /**
     * 通过分类id查询所有商品
     *
     * @param categoryId 分类id
     * @return 商品列表
     */
    List<Commodity> findByCategoryIdIs(Integer categoryId);

    /**
     * 通过主键id查询未删除的商品
     *
     * @param commodityId 商品id
     * @return 未删除的商品
     */
    Commodity findByCommodityIdIsAndDeletedIsFalse(Integer commodityId);

    /**
     * 通过商品标题模糊查询商品
     *
     * @param commodityTitle 商品标题
     * @param pageable       分页信息
     * @return 分页商品
     */
    Page<Commodity> findByCommodityTitleLikeAndDeletedIsFalse(String commodityTitle, Pageable pageable);
}
