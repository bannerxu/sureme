package top.xuguoliang.service.article;

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
import top.xuguoliang.models.article.*;
import top.xuguoliang.models.comment.ArticleComment;
import top.xuguoliang.models.comment.ArticleCommentDao;
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityBanner;
import top.xuguoliang.models.commodity.CommodityBannerDao;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.manager.Manager;
import top.xuguoliang.models.manager.ManagerDao;
import top.xuguoliang.models.relation.RelationArticleCommodity;
import top.xuguoliang.models.relation.RelationArticleCommodityDao;
import top.xuguoliang.service.article.web.ArticleWebCommodityVO;
import top.xuguoliang.service.article.web.ArticleWebDetailVO;
import top.xuguoliang.service.article.web.ArticleWebResultVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class ArticleWebService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleWebService.class);

    @Resource
    private CommonSpecUtil<Article> commonSpecUtil;

    @Resource
    private ArticleDao articleDao;

    @Resource
    private ArticleBannerDao articleBannerDao;

    @Resource
    private ArticleCommentDao articleCommentDao;

    @Resource
    private RelationArticleCommodityDao relationArticleCommodityDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private ManagerDao managerDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

    @Resource
    private ArticleLikeDao articleLikeDao;

    @Resource
    private ArticleStarDao articleStarDao;

    /**
     * 分页查询文章
     *
     * @param articleType 文章类型，为null则查全部
     * @param pageable    分页信息
     * @return 分页文章
     */
    public Page<ArticleWebResultVO> findPage(Integer userId, ArticleTypeEnum articleType, Pageable pageable) {
        Page<Article> articles;
        Specification<Article> deleted = commonSpecUtil.equal("deleted", false);
        Specification<Article> type = commonSpecUtil.equal("articleType", articleType);
        if (ObjectUtils.isEmpty(articleType)) {
            // 查询所有
            articles = articleDao.findAll(deleted, pageable);
        } else {
            // 查询指定类型
            Specifications<Article> specifications = Specifications.where(deleted).and(type);
            articles = articleDao.findAll(specifications, pageable);
        }

        if (ObjectUtils.isEmpty(articles)) {
            return null;
        }
        return articles.map(article -> {
            ArticleWebResultVO vo = new ArticleWebResultVO();
            if (!ObjectUtils.isEmpty(article)) {
                Integer articleId = article.getArticleId();
                BeanUtils.copyNonNullProperties(article, vo);

                // 发布者名称
                Integer managerId = article.getManagerId();
                Manager manager = managerDao.findOne(managerId);
                String name = manager.getName();
                vo.setManagerName(name);

                // 查询点赞和收藏
                ArticleLike articleLike = articleLikeDao.findByArticleIdIsAndUserIdIs(articleId, userId);
                if (!ObjectUtils.isEmpty(articleLike)) {
                    vo.setIsLike(true);
                }
                ArticleStar articleStar = articleStarDao.findByArticleIdIsAndUserIdIs(articleId, userId);
                if(!ObjectUtils.isEmpty(articleStar)) {
                    vo.setIsStar(true);
                }
                return vo;
            } else {
                return null;
            }
        });
    }

    /**
     * 文章详情
     *
     * @param userId 用户id
     * @param articleId 文章id
     * @return 文章
     */
    public ArticleWebDetailVO getDetail(Integer userId, Integer articleId) {

        Article article = articleDao.findByArticleIdIsAndDeletedIsFalse(articleId);
        // 判断文章非空
        if (ObjectUtils.isEmpty(article)) {
            logger.error("查询文章详情失败：id:{}对应的文章不存在", article);
            throw new ValidationException(MessageCodes.WEB_ARTICLE_NOT_EXIST);
        }

        // 查询文章轮播
        List<ArticleBanner> articleBanners = articleBannerDao.findByArticleIdIsAndDeletedIsFalseOrderByArticleBannerIdAsc(articleId);

        // 查询商品以及商品的图片
        List<RelationArticleCommodity> relations = relationArticleCommodityDao.findByArticleIdIsAndDeletedIsFalse(articleId);
        List<Commodity> commodities = new ArrayList<>();
        List<ArticleWebCommodityVO> vos = new ArrayList<>();

        // 通过关系表取出所有商品
        relations.forEach(relation -> {
            Integer commodityId = relation.getCommodityId();
            Commodity commodity = commodityDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
            if (!ObjectUtils.isEmpty(commodity)) {
                commodities.add(commodity);
            }
        });

        // 通过每个商品id去查询轮播图，并把第一张放到返回值里
        if (!ObjectUtils.isEmpty(commodities)) {
            commodities.forEach(commodity -> {
                ArticleWebCommodityVO vo = new ArticleWebCommodityVO();
                Integer commodityId = commodity.getCommodityId();
                List<CommodityBanner> commodityBanners = commodityBannerDao
                        .findByCommodityIdIsAndDeletedIsFalseOrderByCommodityIdAsc(commodityId);
                vo.setCommodityBanner(commodityBanners.get(0));
                vos.add(vo);
            });
        }

        // 发布者名称
        Integer managerId = article.getManagerId();
        Manager manager = managerDao.findOne(managerId);
        String name = manager.getName();

        ArticleWebDetailVO resultVO = new ArticleWebDetailVO();

        // 查询点赞和收藏
        ArticleLike articleLike = articleLikeDao.findByArticleIdIsAndUserIdIs(articleId, userId);
        if (!ObjectUtils.isEmpty(articleLike)) {
            resultVO.setIsLike(true);
        }
        ArticleStar articleStar = articleStarDao.findByArticleIdIsAndUserIdIs(articleId, userId);
        if(!ObjectUtils.isEmpty(articleStar)) {
            resultVO.setIsStar(true);
        }

        BeanUtils.copyNonNullProperties(article, resultVO);
        resultVO.setCommodities(vos);
        resultVO.setArticleBanners(articleBanners);
        resultVO.setManagerName(name);

        return resultVO;
    }

    /**
     * 分页获取文章评论
     *
     * @param articleId 文章id
     * @param pageable 分页信息
     * @return 文章评论
     */
    public Page<ArticleComment> findCommentPage(Integer articleId, Pageable pageable) {
        return articleCommentDao.findByArticleIdIsAndDeletedIsFalseOrderByCreateTimeDesc(articleId, pageable);
    }

    /**
     * 收藏或取消收藏
     * @param userId 用户id
     * @param articleId 文章id
     * @return 成功与否
     */
    public Boolean star(Integer userId, Integer articleId) {
        Article article = articleDao.findByArticleIdIsAndDeletedIsFalse(articleId);
        if (ObjectUtils.isEmpty(article)) {
            logger.error("收藏失败，文章不存在");
            throw new ValidationException(MessageCodes.WEB_ARTICLE_NOT_EXIST);
        }

        ArticleStar articleStar = articleStarDao.findByArticleIdIsAndUserIdIs(articleId, userId);
        if (ObjectUtils.isEmpty(articleStar)) {
            articleStar = new ArticleStar();
            articleStar.setArticleId(articleId);
            articleStar.setUserId(userId);
            article.setStarCount(article.getStarCount() + 1);
            articleStarDao.saveAndFlush(articleStar);
            articleDao.saveAndFlush(article);
        } else {
            article.setStarCount(article.getStarCount() - 1);
            articleStarDao.delete(articleStar);
        }

        return true;
    }

    /**
     * 点赞或取消点赞
     * @param userId 用户id
     * @param articleId 文章id
     * @return 成功与否
     */
    public Boolean like(Integer userId, Integer articleId) {
        Article article = articleDao.findByArticleIdIsAndDeletedIsFalse(articleId);
        if (ObjectUtils.isEmpty(article)) {
            logger.error("收藏失败，文章不存在");
            throw new ValidationException(MessageCodes.WEB_ARTICLE_NOT_EXIST);
        }

        ArticleLike articleLike = articleLikeDao.findByArticleIdIsAndUserIdIs(articleId, userId);
        if (ObjectUtils.isEmpty(articleLike)) {
            articleLike = new ArticleLike();
            articleLike.setUserId(userId);
            articleLike.setArticleId(articleId);
            article.setLikeCount(article.getStarCount() + 1);
            articleLikeDao.saveAndFlush(articleLike);
            articleDao.saveAndFlush(article);
        } else {
            article.setLikeCount(article.getLikeCount() - 1);
            articleLikeDao.delete(articleLike);
        }
        return true;
    }
}
