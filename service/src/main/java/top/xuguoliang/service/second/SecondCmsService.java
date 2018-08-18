package top.xuguoliang.service.second;

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
import top.xuguoliang.models.second.Second;
import top.xuguoliang.models.second.SecondDao;
import top.xuguoliang.service.second.cms.SecondCmsAddParamVO;
import top.xuguoliang.service.second.cms.SecondCmsAddResultVO;
import top.xuguoliang.service.second.cms.SecondCmsDetailVO;
import top.xuguoliang.service.second.cms.SecondCmsPageResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class SecondCmsService {

    private static final Logger logger = LoggerFactory.getLogger(SecondCmsService.class);

    @Resource
    private SecondDao secondDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;

    @Resource
    private CommonSpecUtil<Second> commonSpecUtil;

    /**
     * 分页查询秒杀
     *
     * @param pageable 分页信息
     * @return 分页秒杀
     */
    public Page<SecondCmsPageResultVO> findPage(Pageable pageable) {
        Specification<Second> deleted = commonSpecUtil.equal("deleted", false);
        return secondDao.findAll(deleted, pageable).map(second -> {
            SecondCmsPageResultVO vo = new SecondCmsPageResultVO();
            BeanUtils.copyNonNullProperties(second, vo);
            return vo;
        });
    }


    /**
     * 查询秒杀详情
     *
     * @param secondId 秒杀id
     * @return 秒杀详情
     */
    public SecondCmsDetailVO getSecondDetail(Integer secondId) {
        Second second = secondDao.findOne(secondId);
        if (ObjectUtils.isEmpty(second) || second.getDeleted()) {
            logger.error("查询秒杀详情失败：秒杀不存在");
            throw new ValidationException(MessageCodes.CMS_SECOND_NOT_EXIST);
        }

        SecondCmsDetailVO detailVO = new SecondCmsDetailVO();
        BeanUtils.copyNonNullProperties(second, detailVO);
        return detailVO;
    }


    /**
     * 添加秒杀
     *
     * @param paramVO 添加参数
     * @return 添加完成
     */
    public SecondCmsAddResultVO addSecond(SecondCmsAddParamVO paramVO) {
        Second second = new Second();

        // 商品信息
        Integer commodityId = paramVO.getCommodityId();
        Commodity commodity = commodityDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        if (ObjectUtils.isEmpty(commodity)) {
            logger.error("添加秒杀失败：商品{} 不存在");
            throw new ValidationException(MessageCodes.CMS_COMMODITY_NOT_EXIST);
        }
        BeanUtils.copyNonNullProperties(commodity, second);

        // 规格信息
        Integer stockKeepingUnitId = paramVO.getStockKeepingUnitId();
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.findOne(stockKeepingUnitId);
        if (ObjectUtils.isEmpty(stockKeepingUnit) || stockKeepingUnit.getDeleted()) {
            logger.error("添加秒杀失败：规格{} 不存在");
            throw new ValidationException(MessageCodes.CMS_STOCK_KEEPING_UNIT_NOT_EXIST);
        }
        BeanUtils.copyNonNullProperties(commodity, second);
        BeanUtils.copyNonNullProperties(stockKeepingUnit, second);
        BeanUtils.copyNonNullProperties(paramVO, second);
        Second secondSave = secondDao.saveAndFlush(second);

        SecondCmsAddResultVO resultVO = new SecondCmsAddResultVO();
        BeanUtils.copyNonNullProperties(secondSave, resultVO);
        return resultVO;
    }


    /**
     * 删除秒杀，只能删除没有开始的秒杀
     *
     * @param secondId 秒杀id
     */
    public void deleteSecond(Integer secondId) {
        Second second = secondDao.findOne(secondId);
        if (ObjectUtils.isEmpty(second) || second.getDeleted()) {
            logger.warn("删除秒杀失败：不存在或已删除");
            return ;
        }
        second.setDeleted(true);
        secondDao.saveAndFlush(second);
    }

}
