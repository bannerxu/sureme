package top.banner.models.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface CommodityCommentDao extends JpaSpecificationExecutor<CommodityComment>, JpaRepository<CommodityComment, Integer> {
    /**
     * 根据商品id查询所有未删除的商品评论
     *
     * @param commodityId 商品id
     * @return 商品评论
     */
    List<CommodityComment> findByCommodityIdIsAndDeletedIsFalse(Integer commodityId);

    /**
     * 根据商品id分页查询所有未删除的商品评论
     * @param commodityId 商品id
     * @param pageable 分页信息
     * @return 商品评论
     */
    Page<CommodityComment> findAllByCommodityIdIsAndDeletedIsFalse(Integer commodityId, Pageable pageable);
}
