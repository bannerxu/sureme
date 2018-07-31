package top.xuguoliang.models.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface RelationArticleCommodityDao extends JpaSpecificationExecutor<RelationArticleCommodity>, JpaRepository<RelationArticleCommodity, Integer> {
    List<RelationArticleCommodity> findByArticleIdIsAndDeletedIsFalse(Integer articleId);
    List<RelationArticleCommodity> findByCommodityIdIsAndDeletedIsFalse(Integer commodityId);
    RelationArticleCommodity findByArticleIdIsAndCommodityIdIsAndDeletedIsFalse(Integer articleId, Integer commodity);
}
