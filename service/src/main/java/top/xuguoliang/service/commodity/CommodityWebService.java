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
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityBanner;
import top.xuguoliang.models.commodity.CommodityBannerDao;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.coupon.CouponDao;
import top.xuguoliang.service.commodity.web.CommodityWebResultVO;

import javax.annotation.Resource;
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
        return vo;
    }

    /**
     * 获取商品详情（查询单个商品）
     *
     * @param commodityId 商品id
     * @return 商品
     */
    public CommodityWebResultVO getCommodityDetail(Integer commodityId) {
        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("查询商品错误：商品不存在");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
        }
        List<CommodityComment> comments = commodityCommentDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        // todo 查询所有适用于当前商品的卡券，设置到VO中
        CommodityWebResultVO vo = convertCommodityToVO(commodity);
        vo.setComments(comments);
        vo.setCommodityBanners(banners);

        return vo;
    }

}
