package top.xuguoliang.service.coupon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.coupon.Coupon;
import top.xuguoliang.models.coupon.CouponDao;
import top.xuguoliang.models.coupon.PersonalCoupon;
import top.xuguoliang.models.coupon.PersonalCouponDao;
import top.xuguoliang.models.relation.RelationCouponCommodity;
import top.xuguoliang.models.relation.RelationCouponCommodityDao;
import top.xuguoliang.service.coupon.web.CouponWebResultVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class CouponWebService {

    private static final Logger logger = LoggerFactory.getLogger(CouponWebService.class);

    @Resource
    private CommonSpecUtil<Coupon> commonSpecUtil;

    @Resource
    private CouponDao couponDao;

    @Resource
    private PersonalCouponDao personalCouponDao;

    @Resource
    private RelationCouponCommodityDao relationCouponCommodityDao;

    /**
     * 查询指定商品id的所有卡券
     *
     * @param commodityId 商品id
     * @return 卡券列表
     */
    public List<CouponWebResultVO> findAllByCommodityId(Integer commodityId) {
        List<RelationCouponCommodity> relations = relationCouponCommodityDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        if (ObjectUtils.isEmpty(relations)) {
            logger.error("没有该商品的优惠券");
            throw new ValidationException(MessageCodes.WEB_COUPON_NOT_EXIST);
        }
        List<CouponWebResultVO> vos = new ArrayList<>();
        relations.forEach(relationCouponCommodity -> {
            Integer couponId = relationCouponCommodity.getCouponId();
            Coupon coupon = couponDao.findOne(couponId);
            if (ObjectUtils.isEmpty(coupon) || coupon.getDeleted()) {
                logger.warn("id为{}的卡券不存在", couponId);
            } else {
                CouponWebResultVO vo = new CouponWebResultVO();
                BeanUtils.copyNonNullProperties(coupon, vo);
                vos.add(vo);
            }
        });

        return vos;
    }


    /**
     * 领取优惠券
     *
     * @param userId   用户id
     * @param couponId 卡券id
     * @return 成功与否
     */
    public Boolean pullCoupon(Integer userId, Integer couponId) {

        Date date = new Date();

        PersonalCoupon personalCoupon = personalCouponDao.findByUserIdIsAndCouponIdIs(userId, couponId);
        if (!ObjectUtils.isEmpty(personalCoupon) && !personalCoupon.getDeleted()) {
            logger.error("id为{}的用户已经领取过id为{}的优惠券了", userId, couponId);
            throw new ValidationException(MessageCodes.WEB_COUPON_HAS_BEEN_PULLED);
        }

        Coupon coupon = couponDao.findOne(couponId);
        if (ObjectUtils.isEmpty(coupon) || coupon.getDeleted()) {
            logger.error("领券优惠券失败：优惠券不存在");
            throw new ValidationException(MessageCodes.WEB_COUPON_NOT_EXIST);
        }
        // 判断领取时间
        if (!coupon.getPullBeginTime().before(date) && !coupon.getPullEndTime().after(date)) {
            // 不在领取时间内
            logger.error("id为{}的用户想领取不在领取时间内的优惠券{}", userId, couponId);
            throw new ValidationException(MessageCodes.WEB_COUPON_NOT_PULL_TIME);
        }

        PersonalCoupon personal = new PersonalCoupon();
        BeanUtils.copyNonNullProperties(coupon, personal);
        personal.setUserId(userId);
        personal.setCreateTime(date);
        personal.setUpdateTime(date);
        personalCouponDao.saveAndFlush(personal);
        return true;
    }
}
