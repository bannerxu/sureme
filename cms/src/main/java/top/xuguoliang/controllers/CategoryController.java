package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.category.CategoryCmsService;
import top.xuguoliang.service.category.cms.CategoryCmsAddParamVO;
import top.xuguoliang.service.category.cms.CategoryCmsResultVO;
import top.xuguoliang.service.category.cms.CategoryCmsUpdateParamVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/category")
@Api(tags = "分类模块")
public class CategoryController {

    @Resource
    private CategoryCmsService categoryCmsService;

    @GetMapping
    @ApiOperation("分页")
    public Page<CategoryCmsResultVO> findPage(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return categoryCmsService.findPage(pageable);
    }

    @GetMapping("/{categoryId}")
    @ApiOperation("查询单个")
    public CategoryCmsResultVO getCategory(@PathVariable Integer categoryId) {
        return categoryCmsService.getCategory(categoryId);
    }

    @PostMapping
    @ApiOperation("添加")
    public CategoryCmsResultVO addCategory(@RequestBody CategoryCmsAddParamVO categoryCmsAddParamVO) {
        return categoryCmsService.addCategory(categoryCmsAddParamVO);
    }

    @PutMapping("/{categoryId}")
    @ApiOperation("修改")
    public CategoryCmsResultVO updateCategory(@PathVariable Integer categoryId, @RequestBody CategoryCmsUpdateParamVO categoryCmsUpdateParamVO) {
        return categoryCmsService.updateCategory(categoryId, categoryCmsUpdateParamVO);
    }

    @DeleteMapping("/{categoryId}")
    @ApiOperation("删除")
    public void deleteCategory(@PathVariable Integer categoryId) {
        categoryCmsService.deleteCategory(categoryId);
    }

}
