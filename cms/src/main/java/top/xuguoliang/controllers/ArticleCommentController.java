package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.comment.ArticleCommentCmsService;
import top.xuguoliang.service.comment.cms.ArticleCommentCmsResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/comment/article")
@Api(tags = "文章评论模块")
public class ArticleCommentController {

    @Resource
    private ArticleCommentCmsService articleCommentCmsService;

    @GetMapping
    public Page<ArticleCommentCmsResultVO> findPage(@RequestParam(required = false) Integer articleId, @PageableDefault Pageable pageable) {
        return articleCommentCmsService.findPage(articleId, pageable);
    }

    @DeleteMapping("/{articleCommentId}")
    public void deleteComment(@PathVariable Integer articleCommentId) {
        articleCommentCmsService.deleteComment(articleCommentId);
    }
}
