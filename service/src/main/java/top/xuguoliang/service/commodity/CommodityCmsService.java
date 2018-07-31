package top.xuguoliang.service.commodity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.commodity.*;
import top.xuguoliang.service.commodity.cms.CommodityCmsAddParamVO;
import top.xuguoliang.service.commodity.cms.CommodityCmsResultVO;
import top.xuguoliang.service.commodity.cms.CommodityCmsUpdateParamVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jinguoguo
 */
@Service
public class CommodityCmsService {

    private Logger logger = LoggerFactory.getLogger(CommodityCmsService.class);

    @Resource
    private CommodityDao commodityDao;

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
            CommodityCmsResultVO commodityCmsResultVO = new CommodityCmsResultVO();
            // 复制商品的属性到返回值
            BeanUtils.copyProperties(commodity, commodityCmsResultVO);

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
        if (ObjectUtils.isEmpty(commodity)) {
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

        // 创建商品类，把参数复制到商品类中
        Commodity commodity = new Commodity();
        BeanUtils.copyNonNullProperties(commodityCmsAddParamVO, commodity);
        // 设置商品属性并保存
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

        // 传入的轮播和规格
        List<CommodityBanner> voCommodityBanners = commodityCmsUpdateParamVO.getCommodityBanners();
        List<StockKeepingUnit> voStockKeepingUnits = commodityCmsUpdateParamVO.getStockKeepingUnits();

        // 数据库中的轮播和规格
        List<CommodityBanner> commodityBanners = commodityBannerDao.findByCommodityIdIs(commodityId);
        List<StockKeepingUnit> stockKeepingUnits = stockKeepingUnitDao.findByCommodityIdIs(commodityId);
        // VO轮播id和VO规格id
        Set<Integer> commodityBannerIds = voCommodityBanners.stream().map(CommodityBanner::getCommodityBannerId).collect(Collectors.toSet());
        Set<Integer> stockKeepingUnitIds = voStockKeepingUnits.stream().map(StockKeepingUnit::getStockKeepingUnitId).collect(Collectors.toSet());

        List<CommodityBanner> needSaveCommodityBanners = new ArrayList<>();
        List<StockKeepingUnit> needSaveStockKeepingUnits = new ArrayList<>();

        commodityBanners.forEach(commodityBanner -> {
            // 所有的全部设置成删除
            commodityBanner.setDeleted(true);
            Integer commodityBannerId = commodityBanner.getCommodityBannerId();
            if (ObjectUtils.isEmpty(commodityBannerId)) {
                // id为空，需要新增
                commodityBanner.setCreateTime(date);
                commodityBanner.setUpdateTime(date);
                commodityBanner.setDeleted(false);
                commodityBanner.setCommodityId(commodityId);
                needSaveCommodityBanners.add(commodityBanner);
            } else {
                // id不为空，需要修改或者删除
                if (commodityBannerIds.contains(commodityBannerId)) {
                    // 包含，需要修改
                    CommodityBanner update = commodityBannerDao.findOne(commodityBannerId);
                    BeanUtils.copyNonNullProperties(commodityBanner, update);
                    update.setUpdateTime(date);
                    update.setDeleted(false);
                    update.setCommodityBannerUrl(commodityBanner.getCommodityBannerUrl());
                    needSaveCommodityBanners.add(update);
                } else {
                    needSaveCommodityBanners.add(commodityBanner);
                }
            }
        });

        stockKeepingUnits.forEach(stockKeepingUnit -> {
            stockKeepingUnit.setDeleted(true);
            Integer stockKeepingUnitId = stockKeepingUnit.getStockKeepingUnitId();
            if (ObjectUtils.isEmpty(stockKeepingUnitId)) {
                // id为空，需要新增
                stockKeepingUnit.setCommodityId(commodityId);
                stockKeepingUnit.setCreateTime(date);
                stockKeepingUnit.setUpdateTime(date);
                stockKeepingUnit.setDeleted(false);
                needSaveStockKeepingUnits.add(stockKeepingUnit);
            } else {
                // id不为空，需要修改或者删除
                if (stockKeepingUnitIds.contains(stockKeepingUnitId)) {
                    // 包含，需要修改
                    StockKeepingUnit update = stockKeepingUnitDao.findOne(stockKeepingUnitId);
                    BeanUtils.copyNonNullProperties(stockKeepingUnit, update);
                    update.setDeleted(false);
                    update.setUpdateTime(date);
                    update.setCommodityId(commodityId);
                    needSaveStockKeepingUnits.add(update);
                } else {
                    // 不包含，需要删除
                    needSaveStockKeepingUnits.add(stockKeepingUnit);
                }
            }
        });

        // 保存所有操作
        commodityBannerDao.save(needSaveCommodityBanners);
        stockKeepingUnitDao.save(needSaveStockKeepingUnits);

        // 设置更新时间
        BeanUtils.copyNonNullProperties(commodityCmsUpdateParamVO, commodity);
        commodity.setUpdateTime(date);
        Commodity commoditySave = commodityDao.saveAndFlush(commodity);

        // 封装返回VO对象
        BeanUtils.copyNonNullProperties(commoditySave, commodityCmsResultVO);

        return commodityCmsResultVO;
    }

    public CommodityCmsResultVO getCommodity(Integer commodityId) {
        CommodityCmsResultVO commodityCmsResultVO = new CommodityCmsResultVO();
        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("调用商品单个查询业务：商品不存在");
            throw new ValidationException(MessageCodes.CMS_COMMODITY_NOT_EXIST, "商品不存在");
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
}
