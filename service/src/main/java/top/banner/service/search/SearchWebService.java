package top.banner.service.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.banner.common.utils.BeanUtils;
import top.banner.common.utils.CommonSpecUtil;
import top.banner.models.article.*;
import top.banner.models.commodity.CommodityBanner;
import top.banner.models.commodity.CommodityBannerDao;
import top.banner.models.commodity.CommodityDao;
import top.banner.models.manager.Manager;
import top.banner.models.manager.ManagerDao;
import top.banner.service.search.web.SearchArticleResultVO;
import top.banner.service.search.web.SearchCommodityResultVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class SearchWebService {

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

    @Resource
    private ArticleDao articleDao;

    @Resource
    private ArticleBannerDao articleBannerDao;

    @Resource
    private CommonSpecUtil<Article> commonSpecUtil;

    @Resource
    private ManagerDao managerDao;


    /**
     * 通过商品标题分页查询商品
     *
     * @param commodityTitle 商品标题
     * @param pageable       分页信息
     * @return 分页商品
     */
    public Page<SearchCommodityResultVO> searchCommodity(String commodityTitle, Pageable pageable) {
        commodityTitle = "%" + commodityTitle + "%";
        return commodityDao.findByCommodityTitleLikeAndDeletedIsFalse(commodityTitle, pageable).map(commodity -> {
            SearchCommodityResultVO resultVO = new SearchCommodityResultVO();
            BeanUtils.copyNonNullProperties(commodity, resultVO);
            Integer commodityId = commodity.getCommodityId();
            List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalseOrderByCommodityBannerIdAsc(commodityId);
            if (!ObjectUtils.isEmpty(banners)) {
                resultVO.setCommodityImage(banners.get(0).getCommodityBannerUrl());
            }

            return resultVO;
        });
    }


    /**
     * 通过文章标题和类型分页查询文章
     *
     * @param articleTitle 文章标题
     * @param articleType  文章类型
     * @param pageable     分页信息
     * @return 分页文章
     */
    public Page<SearchArticleResultVO> searchArticle(String articleTitle, ArticleTypeEnum articleType, Pageable pageable) {
        Specification<Article> sArticleTitle = commonSpecUtil.like("articleTitle", articleTitle);
        if (articleType.equals(ArticleTypeEnum.ALL)) {
            // 查询所有
            return articleDao.findAll(sArticleTitle, pageable).map(this::toArticleVO);
        } else {
            Specification<Article> sArticleType = commonSpecUtil.equal("articleType", articleType);
            Specifications<Article> specifications = Specifications.where(sArticleTitle).and(sArticleType);
            return articleDao.findAll(specifications, pageable).map(this::toArticleVO);
        }
    }


    private SearchArticleResultVO toArticleVO(Article article) {
        if (ObjectUtils.isEmpty(article)) {
            return null;
        }
        SearchArticleResultVO resultVO = new SearchArticleResultVO();
        BeanUtils.copyNonNullProperties(article, resultVO);

        Integer articleId = article.getArticleId();
        List<ArticleBanner> banners = articleBannerDao.findByArticleIdIsAndDeletedIsFalseOrderByArticleBannerIdAsc(articleId);
        if (!ObjectUtils.isEmpty(banners)) {
            resultVO.setArticleImage(banners.get(0).getArticleBannerUrl());
        }

        Integer managerId = article.getManagerId();
        Manager manager = managerDao.getOne(managerId);
        if (!ObjectUtils.isEmpty(manager) && !manager.getDeleted()) {
            resultVO.setManagerName(manager.getName());
        }

        return resultVO;
    }
}
