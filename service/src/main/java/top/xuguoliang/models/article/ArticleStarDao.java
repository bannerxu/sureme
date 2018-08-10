package top.xuguoliang.models.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface ArticleStarDao extends JpaSpecificationExecutor<ArticleStar>, JpaRepository<ArticleStar, Integer> {
    /**
     * 通过文章id和用户id查找收藏
     * @param articleId 文章id
     * @param userId 用户id
     * @return 收藏
     */
    ArticleStar findByArticleIdIsAndUserIdIs(Integer articleId, Integer userId);
}
