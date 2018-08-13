package top.xuguoliang.models.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface ArticleStarDao extends JpaSpecificationExecutor<ArticleStar>, JpaRepository<ArticleStar, Integer> {
    /**
     * 通过文章id和用户id查找收藏
     *
     * @param articleId 文章id
     * @param userId    用户id
     * @return 收藏
     */
    ArticleStar findByArticleIdIsAndUserIdIs(Integer articleId, Integer userId);

    /**
     * 通过用户id分页查找收藏
     *
     * @param userId   用户id
     * @param pageable 分页
     * @return 收藏
     */
    Page<ArticleStar> findByUserIdIs(Integer userId, Pageable pageable);

    /**
     * 通过主键id和用户id查找收藏
     *
     * @param articleStarId 主键id
     * @param userId        用户id
     * @return 文章收藏
     */
    ArticleStar findByArticleStarIdIsAndUserIdIs(Integer articleStarId, Integer userId);
}
