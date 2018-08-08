package top.xuguoliang.service.coupon;

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
import top.xuguoliang.models.coupon.Coupon;
import top.xuguoliang.models.coupon.CouponDao;
import top.xuguoliang.models.relation.RelationCouponCommodity;
import top.xuguoliang.models.relation.RelationCouponCommodityDao;
import top.xuguoliang.service.coupon.cms.CouponCmsAddVO;
import top.xuguoliang.service.coupon.cms.CouponCmsResultVO;
import top.xuguoliang.service.coupon.cms.CouponCmsUpdateVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class CouponCmsService {

    private static final Logger logger = LoggerFactory.getLogger(CouponCmsService.class);

    @Resource
    private CouponDao couponDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private CommonSpecUtil<Coupon> commonSpecUtil;

    @Resource
    private RelationCouponCommodityDao relationCouponCommodityDao;

    /**
     * 分页查询卡券信息
     *
     * @param pageable 分页信息
     * @return 卡券信息
     */
    public Page<CouponCmsResultVO> findPage(Pageable pageable) {
        Specification<Coupon> specification = commonSpecUtil.equal("deleted", false);
        // 查询并转换Coupon到VO并返回
        return couponDao.findAll(specification, pageable).map(this::convertCouponToVO);
    }

    /**
     * 通过id查询单个卡券信息
     *
     * @param couponId 卡券id
     * @return 卡券信息
     */
    public CouponCmsResultVO getCoupon(Integer couponId) {
        Coupon coupon = couponDao.findOne(couponId);
        if (ObjectUtils.isEmpty(coupon) || coupon.isDeleted()) {
            logger.error("调用卡券单个查询业务：id（{}）对应的卡券不存在", couponId);
        }

        return convertCouponToVO(coupon);
    }

    /**
     * 卡券转VO
     *
     * @param coupon 卡券
     * @return VO
     */
    private CouponCmsResultVO convertCouponToVO(Coupon coupon) {
        Integer couponId = coupon.getCouponId();
        CouponCmsResultVO couponCmsResultVO = new CouponCmsResultVO();
        BeanUtils.copyNonNullProperties(coupon, couponCmsResultVO);

        // 查找卡券关联的商品并设置到VO中
        List<RelationCouponCommodity> relations = relationCouponCommodityDao.findByCouponIdIs(couponId);
        List<Commodity> commodities = new ArrayList<>();
        if (!ObjectUtils.isEmpty(relations)) {
            relations.forEach(relationCouponCommodity -> {
                Integer commodityId = relationCouponCommodity.getCommodityId();
                Commodity commodity = commodityDao.findOne(commodityId);
                if (!(ObjectUtils.isEmpty(commodity) || commodity.getDeleted())) {
                    commodities.add(commodity);
                }
            });
        }

        couponCmsResultVO.setCommodities(commodities);
        return couponCmsResultVO;
    }

    /**
     * 添加卡券信息
     *
     * @param couponCmsAddVO 卡券信息
     * @return 添加成功后的卡券信息
     */
    public CouponCmsResultVO addCoupon(CouponCmsAddVO couponCmsAddVO) {

        Date date = new Date();

        Coupon coupon = new Coupon();
        BeanUtils.copyNonNullProperties(couponCmsAddVO, coupon);
        coupon.setCreateTime(date);
        coupon.setUpdateTime(date);
        Coupon couponSave = couponDao.save(coupon);

        List<RelationCouponCommodity> needSaveRelations = new ArrayList<>();
        List<Commodity> resultCommodities = new ArrayList<>();

        List<Integer> commodityIds = couponCmsAddVO.getCommodityIds();
        // 遍历VO中的商品id，设置卡券与商品的关联
        commodityIds.forEach(commodityId -> {
            Commodity commodity = commodityDao.findOne(commodityId);
            if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
                logger.error("调用添加卡券业务：传入的商品不存在，Id为{}", commodityId);
                throw new ValidationException(MessageCodes.CMS_COMMODITY_NOT_EXIST, "商品不存在");
            }
            // 添加商品到返回值
            resultCommodities.add(commodity);
            RelationCouponCommodity relationCouponCommodity = new RelationCouponCommodity();
            relationCouponCommodity.setCouponId(couponSave.getCouponId());
            relationCouponCommodity.setCommodityId(commodityId);
            relationCouponCommodity.setCreateTime(date);
            relationCouponCommodity.setUpdateTime(date);
            // 添加到保存列表
            needSaveRelations.add(relationCouponCommodity);
        });

        // 保存关联
        relationCouponCommodityDao.save(needSaveRelations);
        // 设置返回VO
        CouponCmsResultVO couponCmsResultVO = new CouponCmsResultVO();
        BeanUtils.copyNonNullProperties(couponSave, couponCmsResultVO);
        couponCmsResultVO.setCommodities(resultCommodities);

        return couponCmsResultVO;
    }

    /**
     * 删除id对应的卡券信息
     *
     * @param couponId 卡券id
     */
    public void deleteCoupon(Integer couponId) {
        Coupon coupon = couponDao.findOne(couponId);
        if (ObjectUtils.isEmpty(coupon) || coupon.isDeleted()) {
            logger.error("调用卡券删除业务，id（{}）对应的卡券不存在", couponId);
        }
        // 根据卡券id查询关联关系
        List<RelationCouponCommodity> relation = relationCouponCommodityDao.findByCouponIdIs(couponId);
        if (ObjectUtils.isEmpty(relation)) {
            logger.error("卡券没有关联关系");
        }
        // 删除关联关系
        relation.forEach(relationCouponCommodity -> relationCouponCommodity.setDeleted(true));
        // 删除卡券本身
        coupon.setDeleted(true);
        relationCouponCommodityDao.save(relation);
        couponDao.saveAndFlush(coupon);
    }

    /**
     * 修改卡券信息
     *
     * @param couponCmsUpdateVO 卡券信息
     * @return 修改成功后的卡券信息
     */
    public CouponCmsResultVO updateCoupon(CouponCmsUpdateVO couponCmsUpdateVO) {
        Date date = new Date();

        // 根据VO中的卡券id找到卡券，判断是否为空，非空往下进行
        Integer couponId = couponCmsUpdateVO.getCouponId();
        Coupon coupon = couponDao.findOne(couponId);

        if (ObjectUtils.isEmpty(coupon) || coupon.isDeleted()) {
            logger.error("调用卡券修改业务：id（{}）对应的卡券不存在", couponId);
            throw new ValidationException(MessageCodes.CMS_COUPON_NOT_EXIST, "卡券不存在");
        }

        List<Integer> commodityIds = couponCmsUpdateVO.getCommodityIds();
        // 获取卡券的所有关联关系
        List<RelationCouponCommodity> relations = relationCouponCommodityDao.findByCouponIdIs(couponId);
        // 需要保存的列表
        List<RelationCouponCommodity> needSaveRelation = new ArrayList<>();
        // 返回的商品id
        List<Commodity> resultCommodityList = new ArrayList<>();

        // 遍历卡券的所有关联，该删的删，该加的加
        relations.forEach(relationCouponCommodity -> {
            Integer commodityId = relationCouponCommodity.getCommodityId();
            if (commodityIds.contains(commodityId)) {
                // VO的商品列表中包含当前的商品id，从列表中移除
                commodityIds.remove(commodityId);
                // 查出当前商品放入返回值
                Commodity commodity = commodityDao.findOne(commodityId);
                if (!(ObjectUtils.isEmpty(commodity) || commodity.getDeleted())) {
                    resultCommodityList.add(commodity);
                }
            } else {
                // 不包含，删除关系
                RelationCouponCommodity relation = relationCouponCommodityDao.findByCouponIdIsAndCommodityIdIs(couponId, commodityId);
                relation.setDeleted(true);
                needSaveRelation.add(relation);
            }
        });

        // VO的商品id列表剩下都是新增关联
        if (!ObjectUtils.isEmpty(commodityIds)) {
            commodityIds.forEach(commodityId -> {
                Commodity commodity = commodityDao.findOne(commodityId);
                if (!ObjectUtils.isEmpty(commodity)) {
                    RelationCouponCommodity relation = new RelationCouponCommodity();
                    relation.setCreateTime(date);
                    relation.setUpdateTime(date);
                    relation.setCouponId(couponId);
                    relation.setCommodityId(commodityId);
                    // 添加到保存列表中，统一保存
                    needSaveRelation.add(relation);
                    // 添加到返回值
                    resultCommodityList.add(commodity);
                }
            });
        }

        // 设置卡券信息
        BeanUtils.copyNonNullProperties(couponCmsUpdateVO, coupon);
        coupon.setUpdateTime(date);
        // 统一保存
        relationCouponCommodityDao.save(needSaveRelation);
        couponDao.saveAndFlush(coupon);

        // 设置返回值
        CouponCmsResultVO couponCmsResultVO = new CouponCmsResultVO();
        BeanUtils.copyNonNullProperties(coupon, couponCmsResultVO);
        couponCmsResultVO.setCommodities(resultCommodityList);

        return couponCmsResultVO;
    }
}
