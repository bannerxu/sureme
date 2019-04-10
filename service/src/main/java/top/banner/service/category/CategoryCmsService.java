package top.banner.service.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.banner.common.exception.MessageCodes;
import top.banner.common.exception.ValidationException;
import top.banner.common.utils.BeanUtils;
import top.banner.common.utils.CommonSpecUtil;
import top.banner.models.category.Category;
import top.banner.models.category.CategoryDao;
import top.banner.models.commodity.Commodity;
import top.banner.models.commodity.CommodityDao;
import top.banner.service.category.cms.CategoryCmsAddParamVO;
import top.banner.service.category.cms.CategoryCmsResultVO;
import top.banner.service.category.cms.CategoryCmsUpdateParamVO;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class CategoryCmsService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryCmsService.class);

    @Resource
    private CategoryDao categoryDao;
    @Resource
    private CommodityDao commodityDao;

    @Resource
    private CommonSpecUtil<Category> commonSpecUtil;

    /**
     * 分页查询
     *
     * @param pageable 分页信息
     * @return 分页的结果
     */
    public Page<CategoryCmsResultVO> findPage(Pageable pageable) {
        Specification<Category> deleted = commonSpecUtil.equal("deleted", false);
        return categoryDao.findAll(deleted, pageable).map(category -> {
            CategoryCmsResultVO vo = new CategoryCmsResultVO();
            BeanUtils.copyNonNullProperties(category, vo);
            return vo;
        });
    }

    /**
     * 单个查询
     *
     * @param categoryId id
     * @return 单个结果
     */
    public CategoryCmsResultVO getCategory(Integer categoryId) {
        CategoryCmsResultVO resultVO = new CategoryCmsResultVO();
        Category category = categoryDao.getOne(categoryId);
        BeanUtils.copyNonNullProperties(category, resultVO);
        // todo 执行业务

        return resultVO;
    }

    /**
     * 添加
     *
     * @param addVO 添加的信息
     * @return 添加成功后的实体信息
     */
    public CategoryCmsResultVO addCategory(CategoryCmsAddParamVO addVO) {
        Date date = new Date();
        // 保存
        Category category = new Category();
        BeanUtils.copyNonNullProperties(addVO, category);
        category.setCreateTime(date);
        category.setUpdateTime(date);
        category.setDeleted(false);
        Category categorySave = categoryDao.saveAndFlush(category);
        // 返回值
        CategoryCmsResultVO resultVO = new CategoryCmsResultVO();
        BeanUtils.copyNonNullProperties(categorySave, resultVO);

        return resultVO;
    }

    /**
     * 修改
     *
     * @param updateVO 修改的信息
     * @return 修改成功后的实体信息
     */
    public CategoryCmsResultVO updateCategory(Integer categoryId, CategoryCmsUpdateParamVO updateVO) {
        Date date = new Date();
        // 修改
        Category category = categoryDao.getOne(categoryId);
        if (ObjectUtils.isEmpty(category) || category.getDeleted()) {
            // 实体已被删除
            throw new ValidationException(MessageCodes.CMS_GROUP_BUYING_NOT_EXIST);
        }
        BeanUtils.copyNonNullProperties(updateVO, category);
        category.setUpdateTime(date);
        Category categorySave = categoryDao.saveAndFlush(category);

        // 返回值
        CategoryCmsResultVO resultVO = new CategoryCmsResultVO();
        BeanUtils.copyNonNullProperties(categorySave, resultVO);

        return resultVO;
    }

    /**
     * 删除
     *
     * @param categoryId 实体id
     */
    public void deleteCategory(Integer categoryId) {
        Category category = categoryDao.getOne(categoryId);
        if (ObjectUtils.isEmpty(category)) {
            logger.warn("实体不存在");
            return;
        }
        // 删除前取消掉商品的关联
        List<Commodity> commodities = commodityDao.findByCategoryIdIs(categoryId);
        commodities.forEach(commodity -> commodity.setCategoryId(0));
        commodityDao.saveAll(commodities);

        category.setDeleted(true);
        categoryDao.saveAndFlush(category);
    }

}
