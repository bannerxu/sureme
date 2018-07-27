package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.xuguoliang.models.article.Article;
import top.xuguoliang.service.article.ArticleCmsService;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/article")
@Api(tags = "文章模块")
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Resource
    private ArticleCmsService articleCmsService;

    @GetMapping("page")
    @ApiOperation("分页查询文章")
    public Page<Article> findPage(@PageableDefault Pageable pageable) {
        logger.debug("调用接口：分页查询文章");
        return articleCmsService.findPage(pageable);
    }

    @GetMapping
    @ApiOperation("单个查询")
    public Article getArticle(@RequestParam Integer articleId) {
        return articleCmsService.getArticle(articleId);
    }

}
