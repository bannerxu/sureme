package top.banner.service.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.banner.common.utils.BeanUtils;
import top.banner.common.utils.CommonSpecUtil;
import top.banner.models.article.Article;
import top.banner.models.article.ArticleDao;
import top.banner.models.commodity.Commodity;
import top.banner.models.commodity.CommodityBanner;
import top.banner.models.commodity.CommodityBannerDao;
import top.banner.models.commodity.CommodityDao;
import top.banner.models.manager.ManagerDao;
import top.banner.models.order.OrderDao;
import top.banner.models.order.OrderItemDao;
import top.banner.models.relation.RelationArticleCommodity;
import top.banner.models.relation.RelationArticleCommodityDao;
import top.banner.service.search.cms.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinguoguo
 */
@Service
public class SearchCmsService {

    @Resource
    private ManagerDao managerDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

    @Resource
    private ArticleDao articleDao;

    @Resource
    private CommonSpecUtil<Article> commonSpecUtil;

    @Resource
    private RelationArticleCommodityDao relationArticleCommodityDao;


    public Page<SearchManagerResultVO> searchManager(String name, Pageable pageable) {
        name = "%" + name + "%";
        return managerDao.findByNameLikeAndRoleIsAndDeletedIsFalse(name, 0, pageable).map(manager -> {
            SearchManagerResultVO resultVO = new SearchManagerResultVO();
            BeanUtils.copyNonNullProperties(manager, resultVO);
            return resultVO;
        });
    }

    public Page<SearchOrderResultVO> searchOrder(String orderNumber, Pageable pageable) {
        orderNumber = "%" + orderNumber + "%";
        return orderDao.findByOrderNumberLikeAndDeletedIsFalse(orderNumber, pageable).map(order -> {
            SearchOrderResultVO resultVO = new SearchOrderResultVO();
            BeanUtils.copyNonNullProperties(order, resultVO);
            Integer orderId = order.getOrderId();

            // 处理订单条目
            List<SearchOrderItemVO> orderItems = orderItemDao.findByOrderIdIs(orderId).stream().map(orderItem -> {
                SearchOrderItemVO itemVO = new SearchOrderItemVO();
                BeanUtils.copyNonNullProperties(orderItem, itemVO);
                Integer commodityId = orderItem.getCommodityId();
                List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalseOrderByCommodityBannerIdAsc(commodityId);
                if (!ObjectUtils.isEmpty(banners)) {
                    itemVO.setCommodityImage(banners.get(0).getCommodityBannerUrl());
                }
                return itemVO;
            }).collect(Collectors.toList());

            resultVO.setOrderItems(orderItems);
            return resultVO;
        });
    }

    public Page<SearchCommodityResultVO> searchCommodity(String commodityTitle, Pageable pageable) {
        commodityTitle = "%" + commodityTitle + "%";
        return commodityDao.findByCommodityTitleLikeAndDeletedIsFalse(commodityTitle, pageable).map(commodity -> {
            SearchCommodityResultVO resultVO = new SearchCommodityResultVO();
            BeanUtils.copyNonNullProperties(commodity, resultVO);

            return resultVO;
        });
    }

    public Page<SearchArticleResultVO> searchArticle(String articleTitle, Pageable pageable) {
        Specification<Article> sArticleTitle = commonSpecUtil.like("articleTitle", articleTitle);
        Specification<Article> sDeleted = commonSpecUtil.equal("deleted", false);
        Specifications<Article> specifications = Specifications.where(sArticleTitle).and(sDeleted);

        return articleDao.findAll(specifications, pageable).map(article -> {
            SearchArticleResultVO resultVO = new SearchArticleResultVO();
            BeanUtils.copyNonNullProperties(article, resultVO);
            List<SearchArticleCommodityVO> commodities = new ArrayList<>();

            // 查询、设置关联商品
            List<RelationArticleCommodity> relations = relationArticleCommodityDao.findByArticleIdIsAndDeletedIsFalse(article.getArticleId());
            if (!ObjectUtils.isEmpty(relations)) {
                relations.forEach(relation -> {
                    Integer commodityId = relation.getCommodityId();
                    Commodity commodity = commodityDao.getOne(commodityId);
                    if (!ObjectUtils.isEmpty(commodity)) {
                        SearchArticleCommodityVO vo = new SearchArticleCommodityVO();
                        BeanUtils.copyNonNullProperties(commodity, vo);
                        commodities.add(vo);
                    }
                });
            }
            resultVO.setCommodities(commodities);

            return resultVO;
        });
    }
}
