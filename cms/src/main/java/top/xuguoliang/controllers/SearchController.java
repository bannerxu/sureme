package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xuguoliang.service.search.SearchCmsService;
import top.xuguoliang.service.search.cms.SearchArticleResultVO;
import top.xuguoliang.service.search.cms.SearchCommodityResultVO;
import top.xuguoliang.service.search.cms.SearchManagerResultVO;
import top.xuguoliang.service.search.cms.SearchOrderResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/search")
@Api(tags = "搜索模块")
public class SearchController {

    @Resource
    private SearchCmsService searchCmsService;

    @GetMapping("manager/{name}")
    @ApiOperation("管理员搜索")
    public Page<SearchManagerResultVO> searchManager(@PathVariable String name,
                                                     @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return searchCmsService.searchManager(name, pageable);
    }

    @GetMapping("order/{orderNumber}")
    @ApiOperation("订单搜索")
    public Page<SearchOrderResultVO> searchOrder(@PathVariable String orderNumber,
                                           @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return searchCmsService.searchOrder(orderNumber, pageable);
    }

    @GetMapping("commodity/{commodityTitle}")
    @ApiOperation("商品搜索")
    public Page<SearchCommodityResultVO> searchCommodity(@PathVariable String commodityTitle,
                                                   @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return searchCmsService.searchCommodity(commodityTitle, pageable);
    }

    @GetMapping("article/{articleTitle}")
    @ApiOperation("文章搜索")
    public Page<SearchArticleResultVO> searchArticle(@PathVariable String articleTitle,
                                                     @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return searchCmsService.searchArticle(articleTitle, pageable);
    }

}
