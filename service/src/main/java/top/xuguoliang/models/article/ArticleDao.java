package top.xuguoliang.models.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface ArticleDao extends JpaSpecificationExecutor<Article>, JpaRepository<Article, Integer> {

    Article findByArticleIdIsAndDeletedIsFalse(Integer articleId);
}
