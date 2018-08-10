package top.xuguoliang.service.category;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.category.Category;
import top.xuguoliang.models.category.CategoryDao;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class CategoryWebService {

    @Resource
    private CommonSpecUtil<Category> commonSpecUtil;

    @Resource
    private CategoryDao categoryDao;

    /**
     * 查询所有商品分类
     * @return 分类列表
     */
    public List<Category> findAll() {
        Specification<Category> deleted = commonSpecUtil.equal("deleted", false);
        return categoryDao.findAll(deleted);
    }

}
