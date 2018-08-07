package top.xuguoliang.service.groupbuying;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.commodity.StockKeepingUnit;
import top.xuguoliang.models.commodity.StockKeepingUnitDao;
import top.xuguoliang.models.groupbuying.GroupBuying;
import top.xuguoliang.models.groupbuying.GroupBuyingDao;
import top.xuguoliang.models.order.Order;
import top.xuguoliang.models.order.OrderTypeEnum;
import top.xuguoliang.models.relation.RelationUserGroupBuying;
import top.xuguoliang.models.relation.RelationUserGroupBuyingDao;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.groupbuying.web.GroupBuyingWebJoinParamVO;
import top.xuguoliang.service.groupbuying.web.GroupBuyingWebResultVO;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Service
public class GroupBuyingWebService {

    private static final Logger logger = LoggerFactory.getLogger(GroupBuyingWebService.class);

    @Resource
    private CommonSpecUtil<GroupBuying> commonSpecUtil;

    @Resource
    private GroupBuyingDao groupBuyingDao;

    @Resource
    private UserDao userDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;

    @Resource
    private RelationUserGroupBuyingDao relationUserGroupBuyingDao;

    /**
     * 拼团列表
     *
     * @param pageable 分页信息
     * @return 分页拼团列表
     */
    public Page<GroupBuyingWebResultVO> findPage(Pageable pageable) {
        return groupBuyingDao.findAll(pageable).map(this::convertGroupBuyingToVO);
    }

    /**
     * 拼团详情
     *
     * @param groupBuyingId 拼团id
     * @return 拼团信息
     */
    public GroupBuyingWebResultVO getGroupBuying(Integer groupBuyingId) {
        GroupBuying groupBuying = groupBuyingDao.findOne(groupBuyingId);
        if (ObjectUtils.isEmpty(groupBuying) || groupBuying.getDeleted()) {
            logger.error("查询拼团失败：该拼团不存在");
            throw new ValidationException(MessageCodes.WEB_GROUP_BUYING_NOT_EXIST);
        }
        return convertGroupBuyingToVO(groupBuying);
    }

    private GroupBuyingWebResultVO convertGroupBuyingToVO(GroupBuying groupBuying) {
        Date date = new Date();
        if (!(date.before(groupBuying.getEndTime()) && date.after(groupBuying.getBeginTime()))) {
            return null;
        }
        GroupBuyingWebResultVO resultVO = new GroupBuyingWebResultVO();
        // 商品信息
        Integer commodityId = groupBuying.getCommodityId();
        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("商品不存在");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
        }
        // 规格信息
        Integer stockKeepingUnitId = groupBuying.getStockKeepingUnitId();
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.findOne(stockKeepingUnitId);
        if (ObjectUtils.isEmpty(stockKeepingUnit) || stockKeepingUnit.getDeleted()) {
            logger.error("商品规格不存在");
            throw new ValidationException(MessageCodes.WEB_SKU_NOT_EXIST);
        }
        // todo 查询评论

        // 设置返回值
        BeanUtils.copyNonNullProperties(commodity, resultVO);
        BeanUtils.copyNonNullProperties(stockKeepingUnit, resultVO);
        // BeanUtils.copyNonNullProperties(); todo 设置评论

        return resultVO;
    }

    /**
     * 加入拼团
     *
     * @param userId  用户id
     * @param paramVO 其他参数
     * @return 拼团订单信息
     */
    public GroupBuyingWebResultVO joinGroupBuying(Integer userId, GroupBuyingWebJoinParamVO paramVO) {
        Date date = new Date();
        Order order = new Order();
        // 判断拼团是否存在
        Integer groupBuyingId = paramVO.getGroupBuyingId();
        GroupBuying groupBuying = groupBuyingDao.findOne(groupBuyingId);
        if (ObjectUtils.isEmpty(groupBuying) || groupBuying.getDeleted()) {
            logger.error("加入拼团失败：拼团不存在");
            throw new ValidationException(MessageCodes.WEB_GROUP_BUYING_NOT_EXIST);
        }

        User user = userDao.findOne(userId);
        BeanUtils.copyNonNullProperties(user, order);

        // 查询商品规格
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.findOne(paramVO.getStockKeepingUnitId());
        if (ObjectUtils.isEmpty(stockKeepingUnit) || stockKeepingUnit.getDeleted()) {
            logger.error("加入拼团失败：商品规格不存在");
            throw new ValidationException(MessageCodes.WEB_SKU_NOT_EXIST);
        }
        Integer commodityId = stockKeepingUnit.getCommodityId();
        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("商品不存在");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
        }
        // 设置商品信息到订单
        BeanUtils.copyNonNullProperties(commodity, order);
        // 设置规格信息到订单
        BeanUtils.copyNonNullProperties(stockKeepingUnit, order);

        // 设置拼团订单 未付款
        order.setCreateTime(date);
        order.setUpdateTime(date);
        order.setOrderType(OrderTypeEnum.ORDER_TYPE_GROUP);

        // 创建用户和拼团关系表 未付款
        RelationUserGroupBuying relation = new RelationUserGroupBuying();
        relation.setPaid(false);
        relation.setDeleted(false);
        relation.setCreateTime(date);
        relation.setUpdateTime(date);
        relation.setUserId(userId);
        relation.setGroupBuyingId(groupBuyingId);
        RelationUserGroupBuying relationSave = relationUserGroupBuyingDao.saveAndFlush(relation);

        // 设置返回值
        GroupBuyingWebResultVO resultVO = new GroupBuyingWebResultVO();
        BeanUtils.copyNonNullProperties(relationSave, resultVO);

        return resultVO;
    }
}
