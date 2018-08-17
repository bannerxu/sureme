package top.xuguoliang.service.groupbuying;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
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
import top.xuguoliang.common.utils.NumberUtil;
import top.xuguoliang.common.utils.PaymentUtil;
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.commodity.StockKeepingUnit;
import top.xuguoliang.models.commodity.StockKeepingUnitDao;
import top.xuguoliang.models.groupbuying.GroupBuying;
import top.xuguoliang.models.groupbuying.GroupBuyingDao;
import top.xuguoliang.models.groupbuying.UserGroupBuying;
import top.xuguoliang.models.groupbuying.UserGroupBuyingDao;
import top.xuguoliang.models.order.*;
import top.xuguoliang.models.relation.RelationUserGroupBuying;
import top.xuguoliang.models.relation.RelationUserGroupBuyingDao;
import top.xuguoliang.models.user.Address;
import top.xuguoliang.models.user.AddressDao;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.groupbuying.web.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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
    private UserGroupBuyingDao userGroupBuyingDao;

    @Resource
    private UserDao userDao;

    @Resource
    private AddressDao addressDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;


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
     * 开团
     *
     * @param userId  用户id
     * @param paramVO 开团参数
     * @return 预支付
     */
    @Transactional(rollbackOn = Exception.class)
    public UserGroupBuyingWebOpenResultVO openUserGroupBuying(Integer userId, UserGroupBuyingWebOpenParamVO paramVO) {
        Date now = new Date();

        Integer addressId = paramVO.getAddressId();
        Integer groupBuyingId = paramVO.getGroupBuyingId();
        Integer stockKeepingUnitId = paramVO.getStockKeepingUnitId();

        // 查询是否存在拼团
        GroupBuying groupBuying = groupBuyingDao.findOne(groupBuyingId);
        if (ObjectUtils.isEmpty(groupBuying) || groupBuying.getDeleted()) {
            logger.error("开团失败：拼团不存在");
            throw new ValidationException(MessageCodes.WEB_GROUP_BUYING_NOT_EXIST);
        }

        // 判断是否在拼团时间内
        Date beginTime = groupBuying.getBeginTime();
        Date endTime = groupBuying.getEndTime();
        if (beginTime.after(now) || endTime.before(now)) {
            logger.error("开团失败：不在拼团时间内");
            throw new ValidationException(MessageCodes.WEB_GROUP_BUYING_NOT_IN_TIME);
        }

        // 查询收货地址是否存在
        Address address = addressDao.findOne(addressId);
        if (ObjectUtils.isEmpty(address) || address.getDeleted()) {
            logger.error("开团失败：收货地址不存在");
            throw new ValidationException(MessageCodes.WEB_ADDRESS_NOT_EXIST);
        }

        // 查询商品规格和商品是否存在
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.findOne(stockKeepingUnitId);
        if (ObjectUtils.isEmpty(stockKeepingUnit) || stockKeepingUnit.getDeleted()) {
            logger.error("开团失败：规格不存在");
            throw new ValidationException(MessageCodes.WEB_SKU_NOT_EXIST);
        }
        Integer commodityId = stockKeepingUnit.getCommodityId();
        Commodity commodity = commodityDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        if (ObjectUtils.isEmpty(commodity)) {
            logger.error("开团失败：商品不存在");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
        }

        // 查询用户
        User user = userDao.findOne(userId);
        if (ObjectUtils.isEmpty(user) || user.getDeleted()) {
            logger.error("开团失败：用户不存在");
            throw new ValidationException(MessageCodes.WEB_USER_NOT_EXIST);
        }

        // 创建用户拼团
        UserGroupBuying userGroupBuyingNew = new UserGroupBuying();
        BeanUtils.copyNonNullProperties(commodity, userGroupBuyingNew);
        BeanUtils.copyNonNullProperties(groupBuying, userGroupBuyingNew);
        userGroupBuyingNew.setMaxPeopleNumber(groupBuying.getPeopleNumber());
        userGroupBuyingNew.setCurrentPeopleNumber(1);
        userGroupBuyingNew.setSponsorUserId(userId);
        userGroupBuyingNew.setIsFull(false);
        userGroupBuyingNew.setGroupBuyingId(groupBuyingId);
        userGroupBuyingNew.setCreateTime(now);
        userGroupBuyingNew.setUpdateTime(now);
        UserGroupBuying userGroupBuyingSave = userGroupBuyingDao.save(userGroupBuyingNew);

        // 创建拼团订单，保存拼团信息，地址信息
        Order order = new Order();
        BeanUtils.copyNonNullProperties(address, order);
        order.setUserGroupBuyingId(userGroupBuyingSave.getUserGroupBuyingId());
        order.setOrderNumber(NumberUtil.generateOrderNumber("gb"));
        order.setOrderType(OrderTypeEnum.ORDER_TYPE_GROUP);
        order.setOrderStatus(OrderStatusEnum.ORDER_WAITING_PAYMENT);
        order.setTotalMoney(groupBuying.getGroupPrice());
        order.setCreateTime(now);
        order.setUpdateTime(now);
        Order orderSave = orderDao.save(order);

        // 创建订单条目，保存商品信息、规格信息
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyNonNullProperties(stockKeepingUnit, orderItem);
        BeanUtils.copyNonNullProperties(commodity, orderItem);
        orderItem.setOrderId(orderSave.getOrderId());
        orderItemDao.save(orderItem);

        // 构建返回值
        UserGroupBuyingWebOpenResultVO resultVO = new UserGroupBuyingWebOpenResultVO();
        resultVO.setOrderId(orderSave.getOrderId());
        return resultVO;

    }


    /**
     * 加入拼团
     *
     * @param userId  用户id
     * @param paramVO 其他参数
     * @return 拼团订单信息
     */
    @Transactional(rollbackOn = Exception.class)
    public UserGroupBuyingWebResultVO joinUserGroupBuying(Integer userId, UserGroupBuyingWebJoinParamVO paramVO) {

        Date now = new Date();

        Integer addressId = paramVO.getAddressId();
        Integer userGroupBuyingId = paramVO.getUserGroupBuyingId();
        Integer stockKeepingUnitId = paramVO.getStockKeepingUnitId();

        // 查询是否存在用户拼团
        UserGroupBuying userGroupBuying = userGroupBuyingDao.findOne(userGroupBuyingId);
        if (ObjectUtils.isEmpty(userGroupBuying)) {
            logger.error("加入拼团失败：拼团不存在");
            throw new ValidationException(MessageCodes.WEB_GROUP_BUYING_NOT_EXIST);
        }

        // 判断拼团人数是否已满
        Integer currentPeopleNumber = userGroupBuying.getCurrentPeopleNumber();
        Integer maxPeopleNumber = userGroupBuying.getMaxPeopleNumber();
        if (currentPeopleNumber >= maxPeopleNumber) {
            logger.error("加入拼团失败：拼团人数已满");
            throw new ValidationException(MessageCodes.WEB_USER_GROUP_BUYING_PEOPLE_FULL);
        }

        // 判断是否在拼团时间内
        Date beginTime = userGroupBuying.getBeginTime();
        Date endTime = userGroupBuying.getEndTime();
        if (beginTime.after(now) || endTime.before(now)) {
            logger.error("开团失败：不在拼团时间内");
            throw new ValidationException(MessageCodes.WEB_GROUP_BUYING_NOT_IN_TIME);
        }

        // 查询收货地址是否存在
        Address address = addressDao.findOne(addressId);
        if (ObjectUtils.isEmpty(address) || address.getDeleted()) {
            logger.error("开团失败：收货地址不存在");
            throw new ValidationException(MessageCodes.WEB_ADDRESS_NOT_EXIST);
        }

        // 查询商品规格和商品是否存在
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.findOne(stockKeepingUnitId);
        if (ObjectUtils.isEmpty(stockKeepingUnit) || stockKeepingUnit.getDeleted()) {
            logger.error("开团失败：规格不存在");
            throw new ValidationException(MessageCodes.WEB_SKU_NOT_EXIST);
        }
        Integer commodityId = stockKeepingUnit.getCommodityId();
        Commodity commodity = commodityDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        if (ObjectUtils.isEmpty(commodity)) {
            logger.error("开团失败：商品不存在");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
        }

        // 查询用户
        User user = userDao.findOne(userId);
        if (ObjectUtils.isEmpty(user) || user.getDeleted()) {
            logger.error("开团失败：用户不存在");
            throw new ValidationException(MessageCodes.WEB_USER_NOT_EXIST);
        }

        // 修改用户拼团
        int nowPeopleNumber = currentPeopleNumber + 1;
        userGroupBuying.setCurrentPeopleNumber(nowPeopleNumber);
        userGroupBuying.setUpdateTime(now);
        userGroupBuying.setIsFull(nowPeopleNumber >= maxPeopleNumber);
        UserGroupBuying userGroupBuyingSave = userGroupBuyingDao.save(userGroupBuying);

        // 创建拼团订单，保存拼团信息，地址信息
        Order order = new Order();
        BeanUtils.copyNonNullProperties(address, order);
        order.setUserGroupBuyingId(userGroupBuyingSave.getUserGroupBuyingId());
        order.setOrderNumber(NumberUtil.generateOrderNumber("gb"));
        order.setOrderType(OrderTypeEnum.ORDER_TYPE_GROUP);
        order.setOrderStatus(OrderStatusEnum.ORDER_WAITING_PAYMENT);
        order.setTotalMoney(userGroupBuying.getGroupPrice());
        order.setCreateTime(now);
        order.setUpdateTime(now);
        Order orderSave = orderDao.save(order);

        // 创建订单条目，保存商品信息、规格信息
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyNonNullProperties(stockKeepingUnit, orderItem);
        BeanUtils.copyNonNullProperties(commodity, orderItem);
        orderItem.setOrderId(orderSave.getOrderId());
        orderItemDao.save(orderItem);

        // 构建返回值
        UserGroupBuyingWebResultVO resultVO = new UserGroupBuyingWebResultVO();
        resultVO.setOrderId(orderSave.getOrderId());

        return resultVO;
    }
}
