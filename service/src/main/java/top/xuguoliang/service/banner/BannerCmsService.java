package top.xuguoliang.service.banner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.article.Article;
import top.xuguoliang.models.article.ArticleBanner;
import top.xuguoliang.models.article.ArticleBannerDao;
import top.xuguoliang.models.article.ArticleDao;
import top.xuguoliang.models.banner.Banner;
import top.xuguoliang.models.banner.BannerDao;
import top.xuguoliang.models.banner.BannerTypeEnum;
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityBanner;
import top.xuguoliang.models.commodity.CommodityBannerDao;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.service.banner.cms.BannerCmsAddParamVO;
import top.xuguoliang.service.banner.cms.BannerCmsResultVO;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinguoguo
 */
@Service
public class BannerCmsService {

    private static final int MAX_COUNT = 9;

    private static final Logger logger = LoggerFactory.getLogger(BannerCmsService.class);

    @Resource
    private BannerDao bannerDao;

    @Resource
    private ArticleDao articleDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private ArticleBannerDao articleBannerDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

    @Resource
    private CommonSpecUtil<Banner> commonSpecUtil;

    /**
     * 查询指定类型的所有轮播
     *
     * @param bannerType 轮播类型
     * @return 轮播列表
     */
    public List<BannerCmsResultVO> getBanners(BannerTypeEnum bannerType) {
        Specification<Banner> specification = commonSpecUtil.equal("bannerType", bannerType);

        return bannerDao.findAll(specification).stream().map(banner -> {
            BannerCmsResultVO vo = new BannerCmsResultVO();
            BeanUtils.copyNonNullProperties(banner, vo);
            if (bannerType == BannerTypeEnum.ARTICLE_BANNER) {
                ArticleBanner articleBanner = articleBannerDao.findOne(banner.getArticleBannerId());
                vo.setArticleBannerUrl(articleBanner.getArticleBannerUrl());
                return vo;
            } else {
                CommodityBanner commodityBanner = commodityBannerDao.findOne(banner.getCommodityBannerId());
                vo.setCommodityBannerUrl(commodityBanner.getCommodityBannerUrl());
                return vo;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 添加轮播
     *
     * @param addVO 轮播信息
     * @return 添加完成后的轮播信息
     */
    public BannerCmsResultVO addBanner(BannerCmsAddParamVO addVO) {
        // 类型
        BannerTypeEnum bannerType = addVO.getBannerType();
        // 返回值vo
        BannerCmsResultVO vo = new BannerCmsResultVO();

        // 最多只能有9条轮播
        Specification<Banner> specification = commonSpecUtil.equal("bannerType", bannerType);
        long count = bannerDao.count(specification);
        if (count >= MAX_COUNT) {
            logger.error("调用轮播添加业务：最多只能有9条轮播");
            throw new ValidationException(MessageCodes.CMS_BANNER_ERROR, "调用轮播添加业务：最多只能有9条轮播");
        }

        // 根据类型查询文章（商品）和对应的轮播，判断是否存在
        Article article;
        ArticleBanner articleBanner;
        Commodity commodity;
        CommodityBanner commodityBanner;

        if (bannerType == BannerTypeEnum.ARTICLE_BANNER) {
            article = articleDao.findOne(addVO.getArticleId());
            articleBanner = articleBannerDao.findOne(addVO.getArticleBannerId());
            if (ObjectUtils.isEmpty(article)
                    || article.getDeleted()
                    || ObjectUtils.isEmpty(articleBanner)
                    || articleBanner.getDeleted()) {
                logger.error("调用轮播添加业务：文章或文章轮播图不存在");
                throw new ValidationException(MessageCodes.CMS_BANNER_ERROR, "文章或文章轮播图不存在");
            }
            // 设置url
            vo.setArticleBannerUrl(articleBanner.getArticleBannerUrl());
        } else if (bannerType == BannerTypeEnum.COMMODITY_BANNER) {
            commodity = commodityDao.findOne(addVO.getCommodityId());
            commodityBanner = commodityBannerDao.findOne(addVO.getCommodityBannerId());
            if (ObjectUtils.isEmpty(commodity)
                    || commodity.getDeleted()
                    || ObjectUtils.isEmpty(commodityBanner)
                    || commodityBanner.getDeleted()) {
                logger.error("调用轮播添加业务：商品或商品轮播图不存在");
                throw new ValidationException(MessageCodes.CMS_BANNER_ERROR, "商品或商品轮播图不存在");
            }
            // 设置url
            vo.setCommodityBannerUrl(commodityBanner.getCommodityBannerUrl());
        }

        // 创建轮播并添加到数据库
        Banner banner = new Banner();
        BeanUtils.copyNonNullProperties(addVO, banner);
        Banner bannerSave = bannerDao.saveAndFlush(banner);

        // 设置返回值
        BeanUtils.copyNonNullProperties(bannerSave, vo);

        return vo;
    }

    /**
     * 删除轮播
     * @param bannerId 轮播id
     */
    public void deleteBanner(Integer bannerId) {
        bannerDao.delete(bannerId);
    }

}
