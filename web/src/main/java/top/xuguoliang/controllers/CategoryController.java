package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xuguoliang.models.category.Category;
import top.xuguoliang.service.category.CategoryWebService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/category")
@Api(tags = "分类模块")
public class CategoryController {

    @Resource
    private CategoryWebService categoryWebService;

    @GetMapping
    public List<Category> findAll() {
        return categoryWebService.findAll();
    }
}
