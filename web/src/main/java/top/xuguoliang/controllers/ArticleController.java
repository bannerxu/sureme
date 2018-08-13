package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.models.article.ArticleTypeEnum;
import top.xuguoliang.models.comment.ArticleComment;
import top.xuguoliang.service.article.ArticleWebService;
import top.xuguoliang.service.article.web.ArticleCommentWebResultVO;
import top.xuguoliang.service.article.web.ArticleWebDetailVO;
import top.xuguoliang.service.article.web.ArticleWebResultVO;
import top.xuguoliang.service.comment.cms.ArticleCommentCmsResultVO;

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
    public Page<ArticleWebResultVO> findPage(@PathVariable(required = false) ArticleTypeEnum articleType, @PageableDefault Pageable pageable) {
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
    public Page<ArticleCommentWebResultVO> findCommentPage(@PathVariable Integer articleId, @PageableDefault Pageable pageable) {
        return articleWebService.findCommentPage(articleId, pageable);
    }

    @PostMapping("comment/{articleId}")
    @ApiOperation("评论")
    public Boolean addArticleComment(@PathVariable Integer articleId, @RequestParam String commentContent) {
        Integer userId = UserHelper.getUserId();
        return articleWebService.addArticleComment(userId, articleId, commentContent);
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

}
