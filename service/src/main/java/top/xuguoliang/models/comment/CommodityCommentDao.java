package top.xuguoliang.models.comment;

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

}
