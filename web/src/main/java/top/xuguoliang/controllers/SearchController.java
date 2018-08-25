package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.models.article.ArticleTypeEnum;
import top.xuguoliang.service.search.SearchService;
import top.xuguoliang.service.search.web.SearchArticleResultVO;
import top.xuguoliang.service.search.web.SearchCommodityResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/search")
@Api(tags = "搜索模块")
public class SearchController {

    @Resource
    private SearchService searchService;

    @GetMapping("commodity/{commodityTitle}")
    @ApiOperation("搜索商品")
    public Page<SearchCommodityResultVO> searchCommodity(@PathVariable String commodityTitle,
                                                         @PageableDefault Pageable pageable) {
        return searchService.searchCommodity(commodityTitle, pageable);
    }

    @GetMapping("article/{articleTitle}/articleType/{articleType}")
    @ApiOperation("搜索文章")
    public Page<SearchArticleResultVO> searchArticle(@PathVariable String articleTitle,
                                                     @PathVariable ArticleTypeEnum articleType,
                                                     @PageableDefault Pageable pageable) {
        return searchService.searchArticle(articleTitle, articleType, pageable);
    }
}
