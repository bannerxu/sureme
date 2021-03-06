package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.service.comment.CommodityCommentCmsService;
import top.banner.service.comment.cms.CmsReplyCommentParamVO;
import top.banner.service.comment.cms.CommodityCommentCmsResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/comment/commodity")
@Api(tags = "商品评论模块")
public class CommodityCommentController {

    @Resource
    private CommodityCommentCmsService commodityCommentCmsService;

    @GetMapping
    @ApiOperation("分页查询评论")
    public Page<CommodityCommentCmsResultVO> findPage(@RequestParam Integer commodityId,
                                                      @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return commodityCommentCmsService.findPage(commodityId, pageable);
    }


    @DeleteMapping("/{commodityCommentId}")
    @ApiOperation("删除评论")
    public void deleteComment(@PathVariable Integer commodityCommentId) {
        commodityCommentCmsService.deleteComment(commodityCommentId);
    }

    @PutMapping("/{commodityCommentId}")
    @ApiOperation("回复评论")
    public void replyComment(@PathVariable Integer commodityCommentId, @RequestBody CmsReplyCommentParamVO cmsReplyCommentParamVO) {
        commodityCommentCmsService.replyComment(commodityCommentId, cmsReplyCommentParamVO);
    }
}
