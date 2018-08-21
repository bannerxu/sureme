package top.xuguoliang.models.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface ArticleDao extends JpaSpecificationExecutor<Article>, JpaRepository<Article, Integer> {

    Article findByArticleIdIsAndDeletedIsFalse(Integer articleId);

    /**
     * 根据怀孕周数查询文章
     * @param pregnancyWeek 怀孕周数，唯一
     * @return 文章
     */
    List<Article> findAllByPregnancyWeekIsAndDeletedIsFalse(Integer pregnancyWeek);

    List<Article> findAllByArticleTypeIsAndDeletedIsFalse(ArticleTypeEnum articleType);

    List<Article> findAllByBabyDayIsAndDeletedIsFalse(Integer babyDay);
}
