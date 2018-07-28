package top.xuguoliang.models.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface ArticleBannerDao extends JpaSpecificationExecutor<ArticleBanner>, JpaRepository<ArticleBanner, Integer> {
    List<ArticleBanner> findByArticleIdIsAndDeletedIsFalse(Integer articleId);
}
