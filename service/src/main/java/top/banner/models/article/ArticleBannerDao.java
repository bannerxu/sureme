package top.banner.models.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface ArticleBannerDao extends JpaSpecificationExecutor<ArticleBanner>, JpaRepository<ArticleBanner, Integer> {
    /**
     * 通过文章id查询未删除的文章轮播
     * @param articleId 文章id
     * @return 文章轮播list
     */
    List<ArticleBanner> findByArticleIdIsAndDeletedIsFalseOrderByArticleBannerIdAsc(Integer articleId);

    /**
     * 通过文章id查询文章轮播，忽略deleted
     *
     * @param articleId 文章id
     * @return 文章轮播list
     */
    List<ArticleBanner> findByArticleIdIs(Integer articleId);
}
