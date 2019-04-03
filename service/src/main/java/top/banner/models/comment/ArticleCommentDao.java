package top.banner.models.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface ArticleCommentDao extends JpaSpecificationExecutor<ArticleComment>, JpaRepository<ArticleComment, Integer> {
    List<ArticleComment> findByArticleIdIsAndDeletedIsFalse(Integer articleId);

    Page<ArticleComment> findByArticleIdIsAndDeletedIsFalseOrderByCreateTimeDesc(Integer articleId, Pageable pageable);

    ArticleComment findByUserIdIsAndArticleIdIsAndDeletedIsFalse(Integer userId, Integer articleId);
}
