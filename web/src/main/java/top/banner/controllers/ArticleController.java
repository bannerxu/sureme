package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.models.article.Article;
import top.banner.models.article.ArticleTypeEnum;
import top.banner.service.article.ArticleWebService;
import top.banner.service.article.web.ArticleCommentWebAddVO;
import top.banner.service.article.web.ArticleCommentWebResultVO;
import top.banner.service.article.web.ArticleWebDetailVO;
import top.banner.service.article.web.ArticleWebResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/article")
@Api(tags = "文章模块")
public class ArticleController {

    @Resource
    private ArticleWebService articleWebService;

    @GetMapping("type/{articleType}")
    @ApiOperation("分页，类型不传默认查询所有")
    public Page<ArticleWebResultVO> findPage(@PathVariable(required = false) ArticleTypeEnum articleType,
                                             @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Integer userId = UserHelper.getUserId();
        return articleWebService.findPage(userId, articleType, pageable);
    }

    @GetMapping("/{articleId}")
    @ApiOperation("文章详情")
    public ArticleWebDetailVO getDetail(@PathVariable Integer articleId) {
        Integer userId = UserHelper.getUserId();
        return articleWebService.getDetail(userId, articleId);
    }

    @GetMapping("comment/{articleId}")
    @ApiOperation("分页文章评论")
    public Page<ArticleCommentWebResultVO> findCommentPage(@PathVariable Integer articleId,
                                                           @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return articleWebService.findCommentPage(articleId, pageable);
    }

    @PostMapping("comment/{articleId}")
    @ApiOperation("评论")
    public Boolean addArticleComment(@PathVariable Integer articleId, @RequestBody ArticleCommentWebAddVO articleCommentWebAddVO) {
        Integer userId = UserHelper.getUserId();
        return articleWebService.addArticleComment(userId, articleId, articleCommentWebAddVO.getCommentContent());
    }

    @PostMapping("star/{articleId}")
    @ApiOperation("收藏")
    public Boolean star(@PathVariable Integer articleId) {
        Integer userId = UserHelper.getUserId();
        return articleWebService.star(userId, articleId);
    }

    @PostMapping("like/{articleId}")
    @ApiOperation("点赞，喜欢")
    public Boolean like(@PathVariable Integer articleId) {
        Integer userId = UserHelper.getUserId();
        return articleWebService.like(userId, articleId);
    }

    @GetMapping("weekly")
    @ApiOperation("获取每周推荐文章")
    public Article getWeekly() {
        Integer userId = UserHelper.getUserId();
        return articleWebService.getWeekly(userId);
    }

}
