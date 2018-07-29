package top.xuguoliang.service.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.models.article.Article;
import top.xuguoliang.models.article.ArticleBanner;
import top.xuguoliang.models.article.ArticleBannerDao;
import top.xuguoliang.models.article.ArticleDao;
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.relation.RelationArticleCommodity;
import top.xuguoliang.models.relation.RelationArticleCommodityDao;
import top.xuguoliang.service.article.cms.ArticleCmsAddParamVO;
import top.xuguoliang.service.article.cms.ArticleCmsResultVO;
import top.xuguoliang.service.article.cms.ArticleCmsUpdateParamVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class ArticleCmsService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleCmsService.class);

    @Resource
    private ArticleDao articleDao;

    @Resource
    private ArticleBannerDao articleBannerDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private RelationArticleCommodityDao relationArticleCommodityDao;


    /**
     * 分页查询文章，和文章banner
     *
     * @param pageable 分页信息
     * @return 文章分页
     */
    public Page<ArticleCmsResultVO> findPage(Pageable pageable) {
        return articleDao.findAll(pageable).map(this::convert);
    }

    /**
     * 转换 Article 到 ArticleCmsResultVO，中间查询对应的 ArticleBanner 并添加
     *
     * @param article 文章
     * @return ArticleCmsResultVO
     */
    private ArticleCmsResultVO convert(Article article) {
        ArticleCmsResultVO articleCmsResultVO = new ArticleCmsResultVO();
        // 复制文章实体属性到VO
        BeanUtils.copyProperties(article, articleCmsResultVO);

        // 文章id
        Integer articleId = article.getArticleId();

        // 通过文章id查找对应轮播图，并设置到VO
        List<ArticleBanner> articleBanners = articleBannerDao.findByArticleIdIsAndDeletedIsFalse(articleId);
        if (!ObjectUtils.isEmpty(articleBanners)) {
            articleCmsResultVO.setArticleBanners(articleBanners);
        }

        // 通过文章id查找对应的商品，并设置到VO
        List<RelationArticleCommodity> relations = relationArticleCommodityDao.findByArticleIdIsAndDeletedIsFalse(articleId);
        final List<Commodity> commodities = new ArrayList<>();
        if (!ObjectUtils.isEmpty(relations)) {
            relations.forEach((relation) -> {
                Integer commodityId = relation.getCommodityId();
                if (!ObjectUtils.isEmpty(commodityId)) {
                    Commodity commodity = commodityDao.findOne(commodityId);
                    commodities.add(commodity);
                }
            });
        }

        // 设置商品到VO
//        articleCmsResultVO.setCommodities(commodities);

        return articleCmsResultVO;
    }


    /**
     * 查询单个文章(包含Banner）
     *
     * @param articleId 文章id
     * @return 文章（包含Banner）
     */
    public ArticleCmsResultVO getArticle(Integer articleId) {
        ArticleCmsResultVO articleCmsResultVO = new ArticleCmsResultVO();

        if (ObjectUtils.isEmpty(articleId)) {
            logger.error("--> 查询单个文章：文章id不能为空");
            throw new ValidationException(MessageCodes.CMS_ID_EMPTY, "文章id不能为空");
        }

        Article article = articleDao.findOne(articleId);

        List<ArticleBanner> articleBanners = articleBannerDao.findByArticleIdIsAndDeletedIsFalse(articleId);

        // 复制文章属性到VO
        BeanUtils.copyProperties(article, articleCmsResultVO);

        // 设置轮播图
        articleCmsResultVO.setArticleBanners(articleBanners);

        return articleCmsResultVO;
    }

    /**
     * 修改文章
     *
     * @param articleCmsUpdateParamVO 修改信息
     * @return 修改后的文章
     */
    public ArticleCmsResultVO updateArticle(ArticleCmsUpdateParamVO articleCmsUpdateParamVO) {
        List<ArticleBanner> articleBanners = articleCmsUpdateParamVO.getArticleBanners();
        return null;
    }

    /**
     * 添加文章
     *
     * @param articleCmsAddParamVO 添加信息
     * @return 成功添加后的文章
     */
    public ArticleCmsResultVO addArticle(Integer managerId, ArticleCmsAddParamVO articleCmsAddParamVO) {

        Date date = new Date();

        // 方法需要返回的VO以及VO中的属性对象
        ArticleCmsResultVO resultVO = new ArticleCmsResultVO();
        List<ArticleBanner> resultBanners = new ArrayList<>();

        // 创建文章对象，复制VO属性到此对象，设置VO中不包含的属性
        Article article = new Article();
        BeanUtils.copyProperties(articleCmsAddParamVO, article);
        article.setCreateTime(date);
        article.setUpdateTime(date);
        article.setManagerId(managerId);
        Article articleSave = articleDao.save(article);
        // 保存后的文章id
        Integer articleId = articleSave.getArticleId();


        // 遍历并创建文章轮播
        List<String> articleBanners = articleCmsAddParamVO.getArticleBanners();
        articleBanners.forEach(str -> {
            ArticleBanner articleBanner = new ArticleBanner();
            articleBanner.setArticleBannerUrl(str);
            articleBanner.setCreateTime(date);
            articleBanner.setUpdateTime(date);
            articleBanner.setArticleId(articleId);
            ArticleBanner bannerSave = articleBannerDao.save(articleBanner);
            // 添加成功，把banner添加到resultBanners里
            resultBanners.add(bannerSave);
        });

        // 遍历商品，将商品和文章建立关联
        List<Integer> commodityIds = articleCmsAddParamVO.getCommodityIds();
        commodityIds.forEach(commodityId -> {
            // 根据文章id和商品id查找关系
            List<RelationArticleCommodity> relation =
                    relationArticleCommodityDao.findByArticleIdIsAndCommodityIdIsAndDeletedIsFalse(articleId, commodityId);
            if (ObjectUtils.isEmpty(relation)) {
                // 如果关系不存在，建立关系
                RelationArticleCommodity relationArticleCommodity = new RelationArticleCommodity();
                relationArticleCommodity.setArticleId(articleId);
                relationArticleCommodity.setCommodityId(commodityId);
                relationArticleCommodity.setCreateTime(date);
                relationArticleCommodity.setUpdateTime(date);
                relationArticleCommodityDao.save(relationArticleCommodity);
            }
        });

        // 设置返回的VO
        BeanUtils.copyProperties(articleSave, resultVO);
        resultVO.setArticleBanners(resultBanners);
        resultVO.setCommodityIds(commodityIds);

        return resultVO;
    }
}
