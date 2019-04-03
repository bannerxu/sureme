package top.banner.service.groupbuying;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.banner.common.exception.MessageCodes;
import top.banner.common.exception.ValidationException;
import top.banner.common.utils.BeanUtils;
import top.banner.common.utils.NumberUtil;
import top.banner.models.commodity.*;
import top.banner.models.groupbuying.GroupBuying;
import top.banner.models.groupbuying.GroupBuyingDao;
import top.banner.models.groupbuying.UserGroupBuying;
import top.banner.models.groupbuying.UserGroupBuyingDao;
import top.banner.models.order.*;
import top.banner.models.user.Address;
import top.banner.models.user.AddressDao;
import top.banner.models.user.User;
import top.banner.models.user.UserDao;
import top.banner.service.groupbuying.web.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class GroupBuyingWebService {

    private static final Logger logger = LoggerFactory.getLogger(GroupBuyingWebService.class);

    @Resource
    private GroupBuyingDao groupBuyingDao;

    @Resource
    private UserGroupBuyingDao userGroupBuyingDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

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
     * 分页查询拼团列表
     *
     * @param pageable 分页信息
     * @return 分页拼团列表
     */
    public Page<GroupBuyingWebResultVO> findPageGroupBuying(Pageable pageable) {
        Date date = new Date();
        return groupBuyingDao.findAllByBeginTimeBeforeAndEndTimeAfterAndDeletedIsFalseOrderByCreateTimeDesc(date, date, pageable).map(groupBuying -> {
            if (ObjectUtils.isEmpty(groupBuying)) {
                logger.error("拼团不存在");
                return null;
            }

            GroupBuyingWebResultVO resultVO = new GroupBuyingWebResultVO();
            BeanUtils.copyNonNullProperties(groupBuying, resultVO);

            // 商品标题名称
            Integer commodityId = groupBuying.getCommodityId();
            Commodity commodity = commodityDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
            if (!ObjectUtils.isEmpty(commodity)) {
                resultVO.setCommodityName(commodity.getCommodityTitle());
            }

            // 商品图片
            List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalseOrderByCommodityBannerIdAsc(commodityId);
            if (!ObjectUtils.isEmpty(banners)) {
                resultVO.setCommodityImage(banners.get(0).getCommodityBannerUrl());
            }

            resultVO.setMaxPeopleNumber(groupBuying.getPeopleNumber());

            return resultVO;
        });
    }

    /**
     * 拼团详情
     *
     * @param groupBuyingId 拼团id
     * @return 拼团信息
     */
    public GroupBuyingWebDetailVO getGroupBuying(Integer groupBuyingId) {
        GroupBuying groupBuying = groupBuyingDao.getOne(groupBuyingId);
        if (ObjectUtils.isEmpty(groupBuying) || groupBuying.getDeleted()) {
            logger.error("查询拼团失败：该拼团不存在");
            throw new ValidationException(MessageCodes.WEB_GROUP_BUYING_NOT_EXIST);
        }
        return convertGroupBuyingToVO(groupBuying);
    }

    /**
     * 拼团转VO
     *
     * @param groupBuying 拼团
     * @return VO
     */
    private GroupBuyingWebDetailVO convertGroupBuyingToVO(GroupBuying groupBuying) {
        Date date = new Date();
        if (!(date.before(groupBuying.getEndTime()) && date.after(groupBuying.getBeginTime()))) {
            return null;
        }
        GroupBuyingWebDetailVO resultVO = new GroupBuyingWebDetailVO();
        // 商品信息
        Integer commodityId = groupBuying.getCommodityId();
        Commodity commodity = commodityDao.getOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("商品不存在");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
        }
        // 规格信息
        Integer stockKeepingUnitId = groupBuying.getStockKeepingUnitId();
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.getOne(stockKeepingUnitId);
        if (ObjectUtils.isEmpty(stockKeepingUnit) || stockKeepingUnit.getDeleted()) {
            logger.error("商品规格不存在");
            throw new ValidationException(MessageCodes.WEB_SKU_NOT_EXIST);
        }
        // 商品轮播
        List<CommodityBanner> commodityBanners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);

        // 设置返回值
        BeanUtils.copyNonNullProperties(commodity, resultVO);
        BeanUtils.copyNonNullProperties(stockKeepingUnit, resultVO);
        BeanUtils.copyNonNullProperties(groupBuying, resultVO);
        resultVO.setCommodityBanners(commodityBanners);

        return resultVO;
    }

    /**
     * 分页查询用户拼团
     *
     * @param pageable 分页信息
     * @return 用户拼团
     */
    public Page<UserGroupBuyingWebResultVO> findPageUserGroupBuying(Pageable pageable) {
        Date date = new Date();
        return userGroupBuyingDao.findByBeginTimeBeforeAndEndTimeAfterAndIsFullIsFalseOrderByCreateTimeDesc(date, date, pageable).map(userGroupBuying -> {
            UserGroupBuyingWebResultVO resultVO = new UserGroupBuyingWebResultVO();
            if (ObjectUtils.isEmpty(userGroupBuying)) {
                logger.error("用户拼团不存在");
                return null;
            }
            BeanUtils.copyNonNullProperties(userGroupBuying, resultVO);

            // 商品标题名称
            Integer commodityId = userGroupBuying.getCommodityId();
            Commodity commodity = commodityDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
            if (!ObjectUtils.isEmpty(commodity)) {
                resultVO.setCommodityName(commodity.getCommodityTitle());
            }

            // 差人数
            resultVO.setNeedPeopleNumber(userGroupBuying.getMaxPeopleNumber() - userGroupBuying.getCurrentPeopleNumber());

            // 商品图片
            List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalseOrderByCommodityBannerIdAsc(commodityId);
            if (!ObjectUtils.isEmpty(banners)) {
                resultVO.setCommodityImage(banners.get(0).getCommodityBannerUrl());
            }

            // 用户
            Integer userId = userGroupBuying.getSponsorUserId();
            User user = userDao.getOne(userId);
            if (ObjectUtils.isEmpty(user) || user.getDeleted()) {
                logger.error("用户不存在");
            } else {
                resultVO.setAvatarUrl(user.getAvatarUrl());
                resultVO.setNickname(user.getNickName());
            }

            return resultVO;
        });
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
        GroupBuying groupBuying = groupBuyingDao.getOne(groupBuyingId);
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
        Address address = addressDao.getOne(addressId);
        if (ObjectUtils.isEmpty(address) || address.getDeleted()) {
            logger.error("开团失败：收货地址不存在");
            throw new ValidationException(MessageCodes.WEB_ADDRESS_NOT_EXIST);
        }

        // 查询商品规格和商品是否存在
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.getOne(stockKeepingUnitId);
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
        User user = userDao.getOne(userId);
        if (ObjectUtils.isEmpty(user) || user.getDeleted()) {
            logger.error("开团失败：用户不存在");
            throw new ValidationException(MessageCodes.WEB_USER_NOT_EXIST);
        }

        // 创建用户拼团
        UserGroupBuying userGroupBuyingNew = new UserGroupBuying();
        BeanUtils.copyNonNullProperties(user, userGroupBuyingNew);
        BeanUtils.copyNonNullProperties(commodity, userGroupBuyingNew);
        BeanUtils.copyNonNullProperties(groupBuying, userGroupBuyingNew);
        userGroupBuyingNew.setMaxPeopleNumber(groupBuying.getPeopleNumber());
        userGroupBuyingNew.setCurrentPeopleNumber(1);
        userGroupBuyingNew.setSponsorUserId(userId);
        userGroupBuyingNew.setIsFull(false);
        userGroupBuyingNew.setGroupBuyingId(groupBuyingId);
        userGroupBuyingNew.setCreateTime(now);
        userGroupBuyingNew.setUpdateTime(now);
        userGroupBuyingNew.setNickname(user.getNickName());
        UserGroupBuying userGroupBuyingSave = userGroupBuyingDao.save(userGroupBuyingNew);

        // 创建拼团订单，保存拼团信息，地址信息
        Order order = new Order();
        BeanUtils.copyNonNullProperties(userGroupBuyingSave, order);
        BeanUtils.copyNonNullProperties(stockKeepingUnit, order);
        BeanUtils.copyNonNullProperties(commodity, order);
        BeanUtils.copyNonNullProperties(address, order);
        order.setUserGroupBuyingId(userGroupBuyingSave.getUserGroupBuyingId());
        order.setOrderNumber(NumberUtil.generateOrderNumber("gb"));
        order.setOrderType(OrderTypeEnum.ORDER_TYPE_GROUP);
        order.setOrderStatus(OrderStatusEnum.ORDER_WAITING_PAYMENT);
        order.setTotalMoney(groupBuying.getGroupPrice());
        order.setRealPayMoney(groupBuying.getGroupPrice());
        order.setCreateTime(now);
        order.setUpdateTime(now);
        Order orderSave = orderDao.save(order);

        // 创建订单条目，保存商品信息、规格信息
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyNonNullProperties(stockKeepingUnit, orderItem);
        BeanUtils.copyNonNullProperties(commodity, orderItem);
        orderItem.setOrderId(orderSave.getOrderId());
        orderItem.setPrice(userGroupBuyingSave.getGroupPrice());
        orderItem.setCount(1);
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
        UserGroupBuying userGroupBuying = userGroupBuyingDao.getOne(userGroupBuyingId);
        if (ObjectUtils.isEmpty(userGroupBuying)) {
            logger.error("加入拼团失败：拼团不存在");
            throw new ValidationException(MessageCodes.WEB_GROUP_BUYING_NOT_EXIST);
        }

        // 判断是否是自己的团
        if (userGroupBuying.getSponsorUserId().equals(userId)) {
            logger.error("加入拼团失败：用户不能加入自己发起的团");
            throw new ValidationException(MessageCodes.WEB_USER_GROUP_BUYING_OWN);
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
        Address address = addressDao.getOne(addressId);
        if (ObjectUtils.isEmpty(address) || address.getDeleted()) {
            logger.error("开团失败：收货地址不存在");
            throw new ValidationException(MessageCodes.WEB_ADDRESS_NOT_EXIST);
        }

        // 查询商品规格和商品是否存在
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.getOne(stockKeepingUnitId);
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
        User user = userDao.getOne(userId);
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
        BeanUtils.copyNonNullProperties(userGroupBuyingSave, order);
        BeanUtils.copyNonNullProperties(stockKeepingUnit, order);
        BeanUtils.copyNonNullProperties(commodity, order);
        BeanUtils.copyNonNullProperties(address, order);
        order.setUserGroupBuyingId(userGroupBuyingSave.getUserGroupBuyingId());
        order.setOrderNumber(NumberUtil.generateOrderNumber("gb"));
        order.setOrderType(OrderTypeEnum.ORDER_TYPE_GROUP);
        order.setOrderStatus(OrderStatusEnum.ORDER_WAITING_PAYMENT);
        order.setRealPayMoney(userGroupBuying.getGroupPrice());
        order.setTotalMoney(userGroupBuying.getGroupPrice());
        order.setCreateTime(now);
        order.setUpdateTime(now);
        Order orderSave = orderDao.save(order);

        // 创建订单条目，保存商品信息、规格信息
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyNonNullProperties(stockKeepingUnit, orderItem);
        BeanUtils.copyNonNullProperties(commodity, orderItem);
        orderItem.setOrderId(orderSave.getOrderId());
        orderItem.setPrice(userGroupBuyingSave.getGroupPrice());
        orderItem.setCount(1);
        orderItemDao.save(orderItem);

        // 构建返回值
        UserGroupBuyingWebResultVO resultVO = new UserGroupBuyingWebResultVO();
        resultVO.setOrderId(orderSave.getOrderId());

        return resultVO;
    }

    /**
     * 获取用户拼团详情
     *
     * @param userGroupBuyingId 用户拼团id
     * @return 用户拼团详情
     */
    public UserGroupBuyingWebDetailVO getUserGroupBuying(Integer userGroupBuyingId) {
        UserGroupBuying userGroupBuying = userGroupBuyingDao.getOne(userGroupBuyingId);
        if (ObjectUtils.isEmpty(userGroupBuying)) {
            logger.error("用户拼团不存在");
            throw new ValidationException(MessageCodes.WEB_USER_GROUP_BUYING_NOT_EXIST);
        }

        return convertUserGroupBuyingToVO(userGroupBuying);
    }

    /**
     * 用户拼团转VO
     *
     * @param userGroupBuying 用户拼团
     * @return VO
     */
    private UserGroupBuyingWebDetailVO convertUserGroupBuyingToVO(UserGroupBuying userGroupBuying) {
        Date date = new Date();
        if (!(date.before(userGroupBuying.getEndTime()) && date.after(userGroupBuying.getBeginTime()))) {
            return null;
        }
        UserGroupBuyingWebDetailVO detailVO = new UserGroupBuyingWebDetailVO();
        // 商品信息
        Integer commodityId = userGroupBuying.getCommodityId();
        Commodity commodity = commodityDao.getOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("商品不存在");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
        }
        // 规格信息
        Integer stockKeepingUnitId = userGroupBuying.getStockKeepingUnitId();
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.getOne(stockKeepingUnitId);
        if (ObjectUtils.isEmpty(stockKeepingUnit) || stockKeepingUnit.getDeleted()) {
            logger.error("商品规格不存在");
            throw new ValidationException(MessageCodes.WEB_SKU_NOT_EXIST);
        }
        // 商品轮播
        List<CommodityBanner> commodityBanners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);

        // 设置返回值
        BeanUtils.copyNonNullProperties(commodity, detailVO);
        BeanUtils.copyNonNullProperties(stockKeepingUnit, detailVO);
        BeanUtils.copyNonNullProperties(userGroupBuying, detailVO);

        detailVO.setCommodityBanners(commodityBanners);

        return detailVO;
    }
}
