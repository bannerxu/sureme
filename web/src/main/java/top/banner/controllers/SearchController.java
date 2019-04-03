package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.models.article.ArticleTypeEnum;
import top.banner.service.search.SearchWebService;
import top.banner.service.search.web.SearchArticleResultVO;
import top.banner.service.search.web.SearchCommodityResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/search")
@Api(tags = "搜索模块")
public class SearchController {

    @Resource
    private SearchWebService searchWebService;

    @GetMapping("commodity/{commodityTitle}")
    @ApiOperation("搜索商品")
    public Page<SearchCommodityResultVO> searchCommodity(@PathVariable String commodityTitle,
                                                         @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return searchWebService.searchCommodity(commodityTitle, pageable);
    }

    @GetMapping("article/{articleTitle}/articleType/{articleType}")
    @ApiOperation("搜索文章")
    public Page<SearchArticleResultVO> searchArticle(@PathVariable String articleTitle,
                                                     @PathVariable ArticleTypeEnum articleType,
                                                     @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return searchWebService.searchArticle(articleTitle, articleType, pageable);
    }
}
