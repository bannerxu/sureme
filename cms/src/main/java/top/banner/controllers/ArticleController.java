package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.common.constants.RoleConstant;
import top.banner.service.article.ArticleCmsService;
import top.banner.service.article.cms.ArticleCmsAddParamVO;
import top.banner.service.article.cms.ArticleCmsDeleteRelationVO;
import top.banner.service.article.cms.ArticleCmsResultVO;
import top.banner.service.article.cms.ArticleCmsUpdateParamVO;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/article")
@Api(tags = "文章模块")
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Resource
    private ArticleCmsService articleCmsService;

    @GetMapping("page")
    @ApiOperation("分页查询文章")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public Page<ArticleCmsResultVO> findPage(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.debug("-> 调用接口：分页查询文章");
        return articleCmsService.findPage(pageable);
    }

    @GetMapping
    @ApiOperation("单个查询")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public ArticleCmsResultVO getArticle(@RequestParam Integer articleId) {
        logger.debug("-> 调用接口：查询单个文章");
        return articleCmsService.getArticle(articleId);
    }

    @PutMapping
    @ApiOperation("修改")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public ArticleCmsResultVO updateArticle(@RequestBody ArticleCmsUpdateParamVO articleCmsUpdateParamVO) {
        logger.debug("-> 调用接口：修改文章");
        return articleCmsService.updateArticle(articleCmsUpdateParamVO);
    }

    @PostMapping
    @ApiOperation("添加")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public ArticleCmsResultVO addArticle(@RequestBody ArticleCmsAddParamVO articleCmsAddParamVO) {
        Integer managerId = ManagerHelper.getManagerId();
        logger.debug("-> 管理员 {} 调用接口：添加文章", managerId);
        return articleCmsService.addArticle(managerId, articleCmsAddParamVO);
    }

    @DeleteMapping("/{articleId}")
    @ApiOperation("删除")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public void deleteArticle(@PathVariable Integer articleId) {
        articleCmsService.deleteArticle(articleId);
    }

    @DeleteMapping("banner/{articleBannerId}")
    @ApiModelProperty("删除文章轮播")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public boolean deleteArticleBanner(@PathVariable @NotNull Integer articleBannerId) {
        return articleCmsService.deleteArticleBanner(articleBannerId);
    }

    @DeleteMapping("commodity")
    @ApiModelProperty("删除文章与商品的关联")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public boolean removeArticleCommodityRelation(@RequestBody ArticleCmsDeleteRelationVO articleCmsDeleteRelationVO) {
        return articleCmsService.removeArticleCommodityRelation(articleCmsDeleteRelationVO);
    }

}
