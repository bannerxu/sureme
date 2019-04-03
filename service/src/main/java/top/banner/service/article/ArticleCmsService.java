package top.banner.service.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.banner.common.exception.MessageCodes;
import top.banner.common.exception.ValidationException;
import top.banner.common.utils.BeanUtils;
import top.banner.common.utils.CommonSpecUtil;
import top.banner.models.article.Article;
import top.banner.models.article.ArticleBanner;
import top.banner.models.article.ArticleBannerDao;
import top.banner.models.article.ArticleDao;
import top.banner.models.commodity.Commodity;
import top.banner.models.commodity.CommodityDao;
import top.banner.models.manager.Manager;
import top.banner.models.manager.ManagerDao;
import top.banner.models.relation.RelationArticleCommodity;
import top.banner.models.relation.RelationArticleCommodityDao;
import top.banner.service.article.cms.ArticleCmsAddParamVO;
import top.banner.service.article.cms.ArticleCmsDeleteRelationVO;
import top.banner.service.article.cms.ArticleCmsResultVO;
import top.banner.service.article.cms.ArticleCmsUpdateParamVO;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Resource
    private ManagerDao managerDao;

    @Resource
    private CommonSpecUtil<Article> commonSpecUtil;

    /**
     * 分页查询文章，和文章banner
     *
     * @param pageable 分页信息
     * @return 文章分页
     */
    public Page<ArticleCmsResultVO> findPage(Pageable pageable) {
        // 添加查询条件
        Specification<Article> specification = commonSpecUtil.equal("deleted", false);
        return articleDao.findAll(specification, pageable).map(this::convert);
    }

    /**
     * 转换 Article 到 ArticleCmsResultVO，中间查询对应的 ArticleBanner 并添加
     *
     * @param article 文章
     * @return ArticleCmsResultVO
     */
    private ArticleCmsResultVO convert(Article article) {

        // 如果文章为空，直接返回
        if (ObjectUtils.isEmpty(article)) {
            return null;
        }

        // 返回的VO
        ArticleCmsResultVO articleCmsResultVO = new ArticleCmsResultVO();
        // 复制文章属性到VO
        BeanUtils.copyNonNullProperties(article, articleCmsResultVO);

        // 文章id
        Integer articleId = article.getArticleId();

        // 通过文章id查找对应轮播图，并设置到VO
        List<ArticleBanner> articleBanners = articleBannerDao.findByArticleIdIsAndDeletedIsFalseOrderByArticleBannerIdAsc(articleId);
        if (!ObjectUtils.isEmpty(articleBanners)) {
            articleCmsResultVO.setArticleBanners(articleBanners);
        }

        // 通过文章id查找对应的商品，并设置到VO
        List<RelationArticleCommodity> relations = relationArticleCommodityDao.findByArticleIdIsAndDeletedIsFalse(articleId);
        List<Commodity> commodities = relations.stream()
                .map(RelationArticleCommodity::getCommodityId)
                .filter(Objects::nonNull)
                .map((commodityId) -> commodityDao.findOne(commodityId))
                .collect(Collectors.toList());

        // 设置文章中发布者的名称
        Manager manager = managerDao.findOne(article.getManagerId());
        articleCmsResultVO.setName(manager.getName());

        // 设置商品到VO
        articleCmsResultVO.setCommodities(commodities);

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

        List<ArticleBanner> articleBanners = articleBannerDao.findByArticleIdIsAndDeletedIsFalseOrderByArticleBannerIdAsc(articleId);

        // 复制文章属性到VO
        BeanUtils.copyNonNullProperties(article, articleCmsResultVO);

        // 设置轮播图
        articleCmsResultVO.setArticleBanners(articleBanners);

        // 通过文章id查找对应的商品，并设置到VO
        List<RelationArticleCommodity> relations = relationArticleCommodityDao.findByArticleIdIsAndDeletedIsFalse(articleId);
        List<Commodity> commodities = relations.stream()
                .map(RelationArticleCommodity::getCommodityId)
                .filter(Objects::nonNull)
                .map((commodityId) -> commodityDao.findOne(commodityId))
                .collect(Collectors.toList());
        articleCmsResultVO.setCommodities(commodities);

        return articleCmsResultVO;
    }

    /**
     * 修改文章
     *
     * @param articleCmsUpdateParamVO 修改信息
     * @return 修改后的文章
     */
    @Transactional(rollbackOn = Exception.class)
    public ArticleCmsResultVO updateArticle(ArticleCmsUpdateParamVO articleCmsUpdateParamVO) {
        Date date = new Date();
        // 构建返回值
        ArticleCmsResultVO vo = new ArticleCmsResultVO();
        List<ArticleBanner> resultArticleBanner = new ArrayList<>();
        List<Commodity> resultCommodities = new ArrayList<>();
        vo.setArticleBanners(resultArticleBanner);
        vo.setCommodities(resultCommodities);

        Integer articleId = articleCmsUpdateParamVO.getArticleId();
        Article article = articleDao.findOne(articleId);
        if (ObjectUtils.isEmpty(article)) {
            logger.error("调用文章修改业务：id对应的文章不存在");
            throw new ValidationException(MessageCodes.CMS_ARTICLE_NOT_EXIST, "文章不存在");
        }

        List<ArticleBanner> articleBanners = articleCmsUpdateParamVO.getArticleBanners();
        List<Integer> commodityIds = articleCmsUpdateParamVO.getCommodityIds();

        List<ArticleBanner> needSaveArticleBanner = new ArrayList<>();
        List<RelationArticleCommodity> needSaveRelation = new ArrayList<>();

        // 遍历VO文章轮播列表，根据id判断新增还是修改
        articleBanners.forEach(articleBanner -> {
            Integer articleBannerId = articleBanner.getArticleBannerId();
            if (ObjectUtils.isEmpty(articleBannerId)) {
                // id为空，增加文章轮播
                articleBanner.setCreateTime(date);
                articleBanner.setUpdateTime(date);
                articleBanner.setArticleId(articleId);
                // 加入保存列表
                needSaveArticleBanner.add(articleBanner);
                // 添加到返回值
                resultArticleBanner.add(articleBanner);
            } else {
                // id不为空，修改文章轮播
                ArticleBanner update = articleBannerDao.findOne(articleBannerId);
                update.setUpdateTime(date);
                update.setArticleId(articleId);
                // 加入保存列表
                needSaveArticleBanner.add(update);
                // 添加到返回值
                resultArticleBanner.add(articleBanner);
            }
        });

        // 遍历商品id列表，生成文章与商品的关联
        commodityIds.forEach(commodityId -> {
            Commodity commodity = commodityDao.findOne(commodityId);
            if (ObjectUtils.isEmpty(commodity)) {
                logger.error("调用修改文章业务：id对应的商品不存在");
                throw new ValidationException(MessageCodes.CMS_COMMODITY_NOT_EXIST, "id对应的商品不存在");
            }
            RelationArticleCommodity relation =
                    relationArticleCommodityDao.findByArticleIdIsAndCommodityIdIsAndDeletedIsFalse(articleId, commodityId);
            if (ObjectUtils.isEmpty(relation)) {
                // 关联不存在，创建关联
                RelationArticleCommodity relationArticleCommodity = new RelationArticleCommodity();
                relationArticleCommodity.setArticleId(articleId);
                relationArticleCommodity.setCommodityId(commodityId);
                relationArticleCommodity.setCreateTime(date);
                relationArticleCommodity.setUpdateTime(date);
                // 加入保存列表
                needSaveRelation.add(relationArticleCommodity);
                // 添加到返回值
                resultCommodities.add(commodity);
            }
        });

        BeanUtils.copyNonNullProperties(articleCmsUpdateParamVO, article);
        article.setUpdateTime(date);

        // 统一保存
        articleDao.save(article);
        articleBannerDao.save(needSaveArticleBanner);
        relationArticleCommodityDao.save(needSaveRelation);

        return vo;
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
        List<Commodity> commodities = new ArrayList<>();

        // 创建文章对象，复制VO属性到此对象，设置VO中不包含的属性
        Article article = new Article();
        BeanUtils.copyNonNullProperties(articleCmsAddParamVO, article);
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

            // 通过id查找商品，如果商品不存在直接跳过，商品存在才执行后面操作
            Commodity commodity = commodityDao.findOne(commodityId);
            if (!ObjectUtils.isEmpty(commodity)) {
                commodities.add(commodity);

                // 根据文章id和商品id查找关系
                RelationArticleCommodity relation =
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
            }
        });

        // 获取管理员姓名
        Manager manager = managerDao.findOne(managerId);
        String name = manager.getName();

        // 设置返回的VO
        BeanUtils.copyNonNullProperties(articleSave, resultVO);
        resultVO.setArticleBanners(resultBanners);
        resultVO.setCommodities(commodities);
        resultVO.setName(name);

        return resultVO;
    }

    /**
     * 删除文章
     *
     * @param articleId 文章id
     */
    public void deleteArticle(Integer articleId) {
        Article article = articleDao.findOne(articleId);
        if (ObjectUtils.isEmpty(article)) {
            logger.error("---> 调用文章删除业务：参数id对应的文章不存在");
            throw new ValidationException(MessageCodes.CMS_ARTICLE_NOT_EXIST, "文章id对应文章不存在");
        }
        article.setDeleted(true);
        articleDao.saveAndFlush(article);
    }

    /**
     * 根据id删除文章轮播
     *
     * @param articleBannerId 文章轮播id
     * @return 是否成功
     */
    public boolean deleteArticleBanner(Integer articleBannerId) {
        ArticleBanner articleBanner = articleBannerDao.findOne(articleBannerId);
        if (ObjectUtils.isEmpty(articleBanner)) {
            logger.error("调用删除文章轮播业务，id对应的文章轮播不存在");
            return false;
        }
        articleBanner.setDeleted(true);
        articleBannerDao.saveAndFlush(articleBanner);
        return true;
    }

    /**
     * 移除文章与商品的关联
     *
     * @return 是否成功
     */
    public boolean removeArticleCommodityRelation(ArticleCmsDeleteRelationVO vo) {
        Integer articleId = vo.getArticleId();
        Integer commodityId = vo.getCommodityId();

        RelationArticleCommodity relation =
                relationArticleCommodityDao.findByArticleIdIsAndCommodityIdIsAndDeletedIsFalse(articleId, commodityId);
        if (ObjectUtils.isEmpty(relation)) {
            logger.error("文章与商品没有关联");
        }
        relation.setDeleted(true);
        relationArticleCommodityDao.saveAndFlush(relation);

        return true;
    }
}
