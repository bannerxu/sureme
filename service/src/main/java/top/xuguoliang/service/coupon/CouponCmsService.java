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
        if (ObjectUtils.isEmpty(coupon) || coupon.getDeleted()) {
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
        CouponCmsResultVO couponCmsResultVO = new CouponCmsResultVO();
        BeanUtils.copyNonNullProperties(coupon, couponCmsResultVO);

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

        CouponCmsResultVO couponCmsResultVO = new CouponCmsResultVO();
        BeanUtils.copyNonNullProperties(couponSave, couponCmsResultVO);

        return couponCmsResultVO;
    }

    /**
     * 删除id对应的卡券信息
     *
     * @param couponId 卡券id
     */
    public void deleteCoupon(Integer couponId) {
        Coupon coupon = couponDao.findOne(couponId);
        if (ObjectUtils.isEmpty(coupon) || coupon.getDeleted()) {
            logger.error("调用卡券删除业务，id（{}）对应的卡券不存在", couponId);
        }
        coupon.setDeleted(true);
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

        Integer couponId = couponCmsUpdateVO.getCouponId();
        Coupon coupon = couponDao.findOne(couponId);

        if (ObjectUtils.isEmpty(coupon) || coupon.getDeleted()) {
            logger.error("调用卡券修改业务：id（{}）对应的卡券不存在", couponId);
            throw new ValidationException(MessageCodes.CMS_COUPON_NOT_EXIST, "卡券不存在");
        }

        // 设置卡券信息
        BeanUtils.copyNonNullProperties(couponCmsUpdateVO, coupon);
        coupon.setUpdateTime(date);
        couponDao.saveAndFlush(coupon);

        // 设置返回值
        CouponCmsResultVO couponCmsResultVO = new CouponCmsResultVO();
        BeanUtils.copyNonNullProperties(coupon, couponCmsResultVO);

        return couponCmsResultVO;
    }
}
