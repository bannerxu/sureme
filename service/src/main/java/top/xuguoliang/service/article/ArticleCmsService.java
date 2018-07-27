package top.xuguoliang.service.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.models.article.Article;
import top.xuguoliang.models.article.ArticleDao;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class ArticleCmsService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleCmsService.class);

    @Resource
    private ArticleDao articleDao;


    public Page<Article> findPage(Pageable pageable) {
        return articleDao.findAll(pageable);
    }


    public Article getArticle(Integer articleId) {
        if (ObjectUtils.isEmpty(articleId)) {
            logger.error("--> 查询单个文章：文章id不能为空");
            throw new ValidationException(MessageCodes.CMS_ID_EMPTY, "文章id不能为空");
        }
        return articleDao.findOne(articleId);
    }
}
