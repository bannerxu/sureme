package top.banner.models.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface ArticleLikeDao extends JpaSpecificationExecutor<ArticleLike>, JpaRepository<ArticleLike, Integer> {
    /**
     * 根据文章id和用户id查找点赞
     * @param articleId 文章id
     * @param userId 用户id
     * @return 点赞
     */
    ArticleLike findByArticleIdIsAndUserIdIs(Integer articleId, Integer userId);
}
