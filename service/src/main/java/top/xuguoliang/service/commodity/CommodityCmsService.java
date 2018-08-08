package top.xuguoliang.service.commodity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.category.Category;
import top.xuguoliang.models.category.CategoryDao;
import top.xuguoliang.models.commodity.*;
import top.xuguoliang.service.commodity.cms.CommodityCmsAddParamVO;
import top.xuguoliang.service.commodity.cms.CommodityCmsResultVO;
import top.xuguoliang.service.commodity.cms.CommodityCmsUpdateParamVO;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author jinguoguo
 */
@Service
public class CommodityCmsService {

    private Logger logger = LoggerFactory.getLogger(CommodityCmsService.class);

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

    @Resource
    private CommonSpecUtil<Commodity> commonSpecUtil;

    /**
     * 分页查询商品
     *
     * @param pageable 分页信息
     * @return 分页商品信息
     */
    public Page<CommodityCmsResultVO> findPage(Pageable pageable) {
        Specification<Commodity> specification = commonSpecUtil.equal("deleted", false);
        return commodityDao.findAll(specification, pageable).map(commodity -> {
            if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
                return null;
            }
            CommodityCmsResultVO commodityCmsResultVO = new CommodityCmsResultVO();
            // 复制商品的属性到返回值
            BeanUtils.copyNonNullProperties(commodity, commodityCmsResultVO);

            // 设置分类
            Integer categoryId = commodity.getCategoryId();
            if (ObjectUtils.isEmpty(categoryId) || categoryId.equals(0)) {
                commodityCmsResultVO.setCategoryName("未分类");
            }

            Integer commodityId = commodity.getCommodityId();
            // 根据商品id查找商品规格，并放到返回值里
            List<StockKeepingUnit> stockKeepingUnits = stockKeepingUnitDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
            commodityCmsResultVO.setStockKeepingUnits(stockKeepingUnits);
            // 根据商品id查找商品轮播，并放到返回值里
            List<CommodityBanner> commodityBanners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
            commodityCmsResultVO.setCommodityBanners(commodityBanners);

            return commodityCmsResultVO;
        });

    }

    /**
     * 删除商品
     *
     * @param commodityId 商品id
     */
    public void deleteCommodity(Integer commodityId) {
        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("调用商品删除业务：商品不存在");
            throw new ValidationException(MessageCodes.CMS_COMMODITY_NOT_EXIST, "商品不存在");
        }
        // 标记商品为已删除，取消商品和轮播的关联，并把商品的规格标记删除
        commodity.setDeleted(true);
        commodityDao.saveAndFlush(commodity);
        List<CommodityBanner> commodityBanners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        if (!ObjectUtils.isEmpty(commodityBanners)) {
            commodityBanners.forEach(commodityBanner -> commodityBanner.setDeleted(true));
            commodityBannerDao.save(commodityBanners);
        }
        List<StockKeepingUnit> stockKeepingUnits = stockKeepingUnitDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        if (!ObjectUtils.isEmpty(stockKeepingUnits)) {
            stockKeepingUnits.forEach(stockKeepingUnit -> stockKeepingUnit.setDeleted(true));
            stockKeepingUnitDao.save(stockKeepingUnits);
        }
    }

    /**
     * 添加商品
     *
     * @param commodityCmsAddParamVO 商品信息
     * @return 商品信息
     */
    public CommodityCmsResultVO addCommodity(CommodityCmsAddParamVO commodityCmsAddParamVO) {
        Date date = new Date();
        // 创建VO返回对象，并设置属性
        CommodityCmsResultVO commodityCmsResultVO = new CommodityCmsResultVO();
        List<CommodityBanner> resultCommodityBanners = new ArrayList<>();
        List<StockKeepingUnit> resultStockKeepingUnit = new ArrayList<>();
        commodityCmsResultVO.setCommodityBanners(resultCommodityBanners);
        commodityCmsResultVO.setStockKeepingUnits(resultStockKeepingUnit);

        // 判断商品分类是否存在
        Integer categoryId = commodityCmsAddParamVO.getCategoryId();
        Category category = categoryDao.findOne(categoryId);
        if (ObjectUtils.isEmpty(category) || category.getDeleted()) {
            logger.error("添加商品错误：分类不存在");
            throw new ValidationException(MessageCodes.CMS_CATEGORY_NOT_EXIST);
        }

        // 创建商品类，把参数复制到商品类中
        Commodity commodity = new Commodity();
        BeanUtils.copyNonNullProperties(commodityCmsAddParamVO, commodity);
        // 设置商品属性并保存
        commodity.setCategoryName(category.getCategoryName());
        commodity.setCreateTime(date);
        commodity.setUpdateTime(date);
        commodity.setSalesVolume(0);
        Commodity commoditySave = commodityDao.saveAndFlush(commodity);
        Integer commoditySaveId = commoditySave.getCommodityId();

        List<CommodityBanner> commodityBanners = commodityCmsAddParamVO.getCommodityBanners();
        List<StockKeepingUnit> stockKeepingUnits = commodityCmsAddParamVO.getStockKeepingUnits();
        // 如果传进来的轮播图不为空，则进行遍历保存
        if (!ObjectUtils.isEmpty(commodityBanners)) {
            commodityBanners.forEach(commodityBanner -> {
                commodityBanner.setCommodityId(commoditySaveId);
                commodityBanner.setCreateTime(date);
                commodityBanner.setUpdateTime(date);
                CommodityBanner commodityBannerSave = commodityBannerDao.save(commodityBanner);
                resultCommodityBanners.add(commodityBannerSave);
            });
        }

        // 遍历规格，保存到规格表中
        stockKeepingUnits.forEach(stockKeepingUnit -> {
            stockKeepingUnit.setCreateTime(date);
            stockKeepingUnit.setUpdateTime(date);
            stockKeepingUnit.setCommodityId(commoditySaveId);
            StockKeepingUnit stockKeepingUnitSave = stockKeepingUnitDao.save(stockKeepingUnit);
            resultStockKeepingUnit.add(stockKeepingUnitSave);
        });

        return commodityCmsResultVO;
    }

    /**
     * 修改商品信息
     *
     * @param commodityCmsUpdateParamVO 修改信息
     * @return 修改后信息
     */
    public CommodityCmsResultVO update(CommodityCmsUpdateParamVO commodityCmsUpdateParamVO) {
        Date date = new Date();

        CommodityCmsResultVO commodityCmsResultVO = new CommodityCmsResultVO();

        Integer commodityId = commodityCmsUpdateParamVO.getCommodityId();

        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("调用商品修改业务：商品不存在");
            throw new ValidationException(MessageCodes.CMS_COMMODITY_NOT_EXIST, "商品不存在");
        }
        Integer categoryId = commodityCmsUpdateParamVO.getCategoryId();
        String categoryName = null;
        if (!categoryId.equals(commodity.getCategoryId())) {
            Category category = categoryDao.findOne(categoryId);
            if (ObjectUtils.isEmpty(category) || category.getDeleted()) {
                logger.error("修改商品错误：分类不存在");
                throw new ValidationException(MessageCodes.CMS_CATEGORY_NOT_EXIST);
            } else {
                categoryName = category.getCategoryName();
            }
        }

        // 传入的轮播和规格
        List<CommodityBanner> voCommodityBanners = commodityCmsUpdateParamVO.getCommodityBanners();
        List<StockKeepingUnit> voStockKeepingUnits = commodityCmsUpdateParamVO.getStockKeepingUnits();

        List<CommodityBanner> needSaveCommodityBanners = new ArrayList<>();
        List<StockKeepingUnit> needSaveStockKeepingUnits = new ArrayList<>();

        voCommodityBanners.forEach(commodityBanner -> {
            Integer commodityBannerId = commodityBanner.getCommodityBannerId();
            if (ObjectUtils.isEmpty(commodityBannerId)) {
                // id为空，新增
                commodityBanner.setUpdateTime(date);
                commodityBanner.setCreateTime(date);
                commodityBanner.setCommodityId(commodityId);
                // 添加到保存列表
                needSaveCommodityBanners.add(commodityBanner);
            } else {
                // id非空，修改
                CommodityBanner update = commodityBannerDao.findOne(commodityBannerId);
                BeanUtils.copyNonNullProperties(commodityBanner, update);
                update.setUpdateTime(date);
                update.setCommodityId(commodityId);
                // 添加到保存列表
                needSaveCommodityBanners.add(update);
            }
        });

        voStockKeepingUnits.forEach(stockKeepingUnit -> {
            Integer stockKeepingUnitId = stockKeepingUnit.getStockKeepingUnitId();
            if (ObjectUtils.isEmpty(stockKeepingUnitId)) {
                // id为空，新增
                stockKeepingUnit.setCreateTime(date);
                stockKeepingUnit.setUpdateTime(date);
                stockKeepingUnit.setCommodityId(commodityId);
                // 添加到保存列表
                needSaveStockKeepingUnits.add(stockKeepingUnit);
            } else {
                // id非空，修改
                StockKeepingUnit update = stockKeepingUnitDao.findOne(stockKeepingUnitId);
                BeanUtils.copyNonNullProperties(stockKeepingUnit, update);
                update.setUpdateTime(date);
                update.setCommodityId(commodityId);
                // 添加到保存列表
                needSaveStockKeepingUnits.add(update);
            }
        });

        // 统一保存
        commodityBannerDao.save(needSaveCommodityBanners);
        stockKeepingUnitDao.save(needSaveStockKeepingUnits);

        BeanUtils.copyNonNullProperties(commodityCmsUpdateParamVO, commodity);
        // 设置更新时间
        commodity.setUpdateTime(date);
        // 设置分类名称
        if (StringUtils.isEmpty(categoryName)) {
            commodity.setCategoryName(categoryName);
        }
        Commodity commoditySave = commodityDao.saveAndFlush(commodity);

        // 封装返回VO对象
        BeanUtils.copyNonNullProperties(commodityCmsUpdateParamVO, commodityCmsResultVO);
        BeanUtils.copyNonNullProperties(commoditySave, commodityCmsResultVO);

        return commodityCmsResultVO;
    }

    /**
     * 通过id查询单个商品
     *
     * @param commodityId 商品id
     * @return 商品信息
     */
    public CommodityCmsResultVO getCommodity(Integer commodityId) {
        CommodityCmsResultVO commodityCmsResultVO = new CommodityCmsResultVO();
        // 通过传入的id查询商品
        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("调用商品单个查询业务：商品不存在");
            throw new ValidationException(MessageCodes.CMS_COMMODITY_NOT_EXIST, "商品不存在");
        }
        // 设置分类
        if (commodity.getCategoryId().equals(0)) {
            commodity.setCategoryName("未分类");
        }

        BeanUtils.copyNonNullProperties(commodity, commodityCmsResultVO);

        // 根据商品id查找商品规格，并放到返回值里
        List<StockKeepingUnit> stockKeepingUnits = stockKeepingUnitDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        commodityCmsResultVO.setStockKeepingUnits(stockKeepingUnits);
        // 根据商品id查找商品轮播，并放到返回值里
        List<CommodityBanner> commodityBanners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        commodityCmsResultVO.setCommodityBanners(commodityBanners);

        return commodityCmsResultVO;
    }

    /**
     * 通过轮播id删除轮播
     *
     * @param commodityBannerId 轮播id
     * @return 是否成功
     */
    public boolean deleteCommodityBanner(Integer commodityBannerId) {
        CommodityBanner commodityBanner = commodityBannerDao.findOne(commodityBannerId);
        if (ObjectUtils.isEmpty(commodityBanner)) {
            logger.error("id对应的商品轮播图不存在");
            return false;
        }
        commodityBanner.setDeleted(true);
        commodityBannerDao.saveAndFlush(commodityBanner);
        return true;
    }

    /**
     * 通过规格id删除规格
     *
     * @param skuId 规格id
     * @return 是否成功
     */
    public boolean deleteSKU(Integer skuId) {
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.findOne(skuId);
        if (ObjectUtils.isEmpty(stockKeepingUnit)) {
            logger.error("id对应的规格不存在");
            return false;
        }
        stockKeepingUnit.setDeleted(true);
        stockKeepingUnitDao.saveAndFlush(stockKeepingUnit);
        return true;
    }
}
