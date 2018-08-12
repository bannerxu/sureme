package top.xuguoliang.service.commodity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.comment.CommodityComment;
import top.xuguoliang.models.comment.CommodityCommentDao;
import top.xuguoliang.models.commodity.*;
import top.xuguoliang.models.coupon.Coupon;
import top.xuguoliang.models.coupon.CouponDao;
import top.xuguoliang.models.coupon.PersonalCoupon;
import top.xuguoliang.models.coupon.PersonalCouponDao;
import top.xuguoliang.models.relation.RelationCouponCommodity;
import top.xuguoliang.models.relation.RelationCouponCommodityDao;
import top.xuguoliang.service.commodity.web.CommodityWebCouponVO;
import top.xuguoliang.service.commodity.web.CommodityWebDetailVO;
import top.xuguoliang.service.commodity.web.CommodityWebResultVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class CommodityWebService {

    private static final Logger logger = LoggerFactory.getLogger(CommodityWebService.class);

    @Resource
    private CommonSpecUtil<Commodity> commonSpecUtil;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private CommodityCommentDao commodityCommentDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

    @Resource
    private CouponDao couponDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;

    @Resource
    private RelationCouponCommodityDao relationCouponCommodityDao;

    @Resource
    private PersonalCouponDao personalCouponDao;

    /**
     * 分页查询商品
     *
     * @param categoryId 分类id，为空查询所有
     * @param pageable   分页信息
     * @return 分页商品
     */
    public Page<CommodityWebResultVO> findPage(Integer categoryId, Pageable pageable) {
        // 未删除的
        Specification<Commodity> deleted = commonSpecUtil.equal("deleted", false);
        if (ObjectUtils.isEmpty(categoryId)) {
            return commodityDao.findAll(deleted, pageable).map(this::convertCommodityToVO);
        } else {
            // 指定分类
            Specifications<Commodity> spec = Specifications.where(deleted)
                    .and(commonSpecUtil.equal("categoryId", categoryId));
            return commodityDao.findAll(spec, pageable).map(this::convertCommodityToVO);
        }
    }

    /**
     * 商品转VO
     *
     * @param commodity 商品
     * @return VO
     */
    private CommodityWebResultVO convertCommodityToVO(Commodity commodity) {
        CommodityWebResultVO vo = new CommodityWebResultVO();
        BeanUtils.copyNonNullProperties(commodity, vo);

        // 商品图片
        Integer commodityId = commodity.getCommodityId();
        List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalseOrderByCommodityIdAsc(commodityId);
        if (!ObjectUtils.isEmpty(banners)) {
            CommodityBanner commodityBanner = banners.get(0);
            vo.setCommodityImage(commodityBanner.getCommodityBannerUrl());
        }

        return vo;
    }

    /**
     * 获取商品详情（查询单个商品）
     *
     * @param commodityId 商品id
     * @return 商品
     */
    public CommodityWebDetailVO getCommodityDetail(Integer userId, Integer commodityId) {
        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("查询商品错误：商品不存在");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
        }
        List<CommodityComment> comments = commodityCommentDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        List<StockKeepingUnit> skus = stockKeepingUnitDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);

        List<RelationCouponCommodity> relations = relationCouponCommodityDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        List<CommodityWebCouponVO> coupons = new ArrayList<>();
        relations.forEach(relation -> {
            CommodityWebCouponVO couponVO = new CommodityWebCouponVO();
            Integer couponId = relation.getCouponId();
            Coupon coupon = couponDao.findOne(couponId);
            BeanUtils.copyNonNullProperties(coupon, couponVO);
            PersonalCoupon pCoupon = personalCouponDao.findByUserIdIsAndCouponIdIsAndDeletedIsFalse(userId, couponId);
            if (!ObjectUtils.isEmpty(pCoupon)) {
                couponVO.setIsPulled(true);
            }
            coupons.add(couponVO);
        });

        CommodityWebDetailVO vo = new CommodityWebDetailVO();
        BeanUtils.copyNonNullProperties(commodity, vo);
        vo.setComments(comments);
        vo.setCommodityBanners(banners);
        vo.setStockKeepingUnits(skus);
        vo.setCoupons(coupons);

        return vo;
    }

}
