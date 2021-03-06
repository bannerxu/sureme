package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.service.comment.ArticleCommentCmsService;
import top.banner.service.comment.cms.ArticleCommentCmsResultVO;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

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
    @ApiOperation("分页")
    public Page<ArticleCommentCmsResultVO> findPage(@RequestParam(required = false) Integer articleId,
                                                    @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return articleCommentCmsService.findPage(articleId, pageable);
    }

    @DeleteMapping("/{articleCommentId}")
    @ApiOperation("删除")
    public void deleteComment(@PathVariable @NotNull(message = "id不能为空") Integer articleCommentId) {
        articleCommentCmsService.deleteComment(articleCommentId);
    }
}
