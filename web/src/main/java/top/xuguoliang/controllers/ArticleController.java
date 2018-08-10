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
import top.xuguoliang.service.article.web.ArticleWebDetailVO;
import top.xuguoliang.service.article.web.ArticleWebResultVO;

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
        return articleWebService.findPage(articleType, pageable);
    }

    @GetMapping("/{articleId}")
    @ApiOperation("文章详情")
    public ArticleWebDetailVO getDetail(@PathVariable Integer articleId) {
        return articleWebService.getDetail(articleId);
    }

    @GetMapping("comment/articleId")
    @ApiOperation("分页文章评论")
    public Page<ArticleComment> findCommentPage(@PathVariable Integer articleId, @PageableDefault Pageable pageable) {
        return articleWebService.findCommentPage(articleId, pageable);
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