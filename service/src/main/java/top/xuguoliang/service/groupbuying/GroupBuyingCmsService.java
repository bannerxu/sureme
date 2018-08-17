package top.xuguoliang.service.groupbuying;

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
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.commodity.StockKeepingUnit;
import top.xuguoliang.models.commodity.StockKeepingUnitDao;
import top.xuguoliang.models.groupbuying.GroupBuying;
import top.xuguoliang.models.groupbuying.GroupBuyingDao;
import top.xuguoliang.models.groupbuying.UserGroupBuying;
import top.xuguoliang.models.groupbuying.UserGroupBuyingDao;
import top.xuguoliang.service.groupbuying.cms.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Service
public class GroupBuyingCmsService {

    private static final Logger logger = LoggerFactory.getLogger(GroupBuyingCmsService.class);

    @Resource
    private GroupBuyingDao groupBuyingDao;

    @Resource
    private CommonSpecUtil<GroupBuying> commonSpecUtil;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;

    @Resource
    private UserGroupBuyingDao userGroupBuyingDao;


    /**
     * 分页查询
     *
     * @param pageable 分页信息
     * @return 分页的结果
     */
    public Page<GroupBuyingCmsResultVO> findPage(Pageable pageable) {
        Specification<GroupBuying> deleted = commonSpecUtil.equal("deleted", false);
        return groupBuyingDao.findAll(deleted, pageable).map(groupBuying -> {
            GroupBuyingCmsResultVO vo = new GroupBuyingCmsResultVO();
            BeanUtils.copyNonNullProperties(groupBuying, vo);
            return vo;
        });
    }

    /**
     * 单个查询
     *
     * @param groupBuyingId id
     * @return 单个结果
     */
    public GroupBuyingCmsDetailVO getGroupBuying(Integer groupBuyingId) {
        GroupBuyingCmsDetailVO detailVO = new GroupBuyingCmsDetailVO();
        GroupBuying groupBuying = groupBuyingDao.findOne(groupBuyingId);
        BeanUtils.copyNonNullProperties(groupBuying, detailVO);

        return detailVO;
    }

    /**
     * 添加
     *
     * @param addVO 添加的信息
     */
    public void addGroupBuying(GroupBuyingCmsAddParamVO addVO) {
        Date date = new Date();
        // 保存
        GroupBuying groupBuying = new GroupBuying();
        BeanUtils.copyNonNullProperties(addVO, groupBuying);
        groupBuying.setCreateTime(date);
        groupBuying.setUpdateTime(date);
        groupBuying.setDeleted(false);

        // 冗余商品和规格信息到实体中
        Integer commodityId = addVO.getCommodityId();
        Commodity commodity = commodityDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        if (ObjectUtils.isEmpty(commodity)) {
            logger.error("添加拼团失败：商品不存在");
            throw new ValidationException(MessageCodes.CMS_COMMODITY_NOT_EXIST);
        }
        StockKeepingUnit sku = stockKeepingUnitDao.findOne(addVO.getStockKeepingUnitId());
        if (ObjectUtils.isEmpty(sku) || sku.getDeleted()) {
            logger.error("添加拼团失败：商品规格不存在");
            throw new ValidationException(MessageCodes.CMS_STOCK_KEEPING_UNIT_NOT_EXIST);
        }
        BeanUtils.copyNonNullProperties(commodity, groupBuying);
        BeanUtils.copyNonNullProperties(sku, groupBuying);

        groupBuyingDao.saveAndFlush(groupBuying);

    }

    /**
     * 修改
     *
     * @param updateVO 修改的信息
     * @return 修改成功后的实体信息
     */
    public GroupBuyingCmsResultVO updateGroupBuying(Integer groupBuyingId, GroupBuyingCmsUpdateParamVO updateVO) {
        // 修改
        GroupBuying groupBuying = groupBuyingDao.findOne(groupBuyingId);
        if (ObjectUtils.isEmpty(groupBuying) || groupBuying.getDeleted()) {
            logger.error("修改拼团失败：拼团不存在");
            throw new ValidationException(MessageCodes.CMS_GROUP_BUYING_NOT_EXIST);
        }
        BeanUtils.copyNonNullProperties(updateVO, groupBuying);
        GroupBuying groupBuyingSave = groupBuyingDao.saveAndFlush(groupBuying);

        // 返回值
        GroupBuyingCmsResultVO resultVO = new GroupBuyingCmsResultVO();
        BeanUtils.copyNonNullProperties(groupBuyingSave, resultVO);

        return resultVO;
    }

    /**
     * 删除
     *
     * @param groupBuyingId 实体id
     */
    public void deleteGroupBuying(Integer groupBuyingId) {
        GroupBuying groupBuying = groupBuyingDao.findOne(groupBuyingId);
        if (ObjectUtils.isEmpty(groupBuying)) {
            logger.warn("删除拼团失败：拼团不存在");
            return;
        }
        groupBuying.setDeleted(true);
        groupBuyingDao.saveAndFlush(groupBuying);
    }

    /**
     * 分页查询用户拼团
     *
     * @param pageable 分页信息
     * @return 用户拼团分页
     */
    public Page<UserGroupBuyingCmsResultVO> findPageUserGroupBuying(Pageable pageable) {
        return userGroupBuyingDao.findByIsFullIsTrue(pageable).map(userGroupBuying -> {
            UserGroupBuyingCmsResultVO resultVO = new UserGroupBuyingCmsResultVO();
            BeanUtils.copyNonNullProperties(userGroupBuying, resultVO);
            return resultVO;
        });

    }

}
