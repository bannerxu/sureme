package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.comment.CommodityCommentCmsService;
import top.xuguoliang.service.comment.cms.CommodityCommentCmsResultVO;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/comment/commodity")
@Api(tags = "商品评论模块")
public class CommodityCommentController {

    private CommodityCommentCmsService commodityCommentCmsService;

    @GetMapping
    public Page<CommodityCommentCmsResultVO> findPage(@RequestParam Integer commodityId,
                                                      @PageableDefault Pageable pageable) {
        return commodityCommentCmsService.findPage(commodityId, pageable);
    }


    @DeleteMapping("/{commodityCommentId}")
    public void deleteComment(@PathVariable Integer commodityCommentId) {
        commodityCommentCmsService.deleteComment(commodityCommentId);
    }
}
