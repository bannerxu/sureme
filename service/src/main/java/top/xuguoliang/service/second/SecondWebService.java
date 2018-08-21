package top.xuguoliang.service.second;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.common.utils.NumberUtil;
import top.xuguoliang.models.commodity.*;
import top.xuguoliang.models.order.*;
import top.xuguoliang.models.second.Second;
import top.xuguoliang.models.second.SecondDao;
import top.xuguoliang.models.user.Address;
import top.xuguoliang.models.user.AddressDao;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.RedisKeyPrefix;
import top.xuguoliang.service.second.web.SecondKillParamVO;
import top.xuguoliang.service.second.web.SecondKillResultVO;
import top.xuguoliang.service.second.web.SecondWebDetailVO;
import top.xuguoliang.service.second.web.SecondWebPageResultVO;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class SecondWebService {

    private static final Logger logger = LoggerFactory.getLogger(SecondWebService.class);

    @Resource
    private SecondDao secondDao;

    @Resource
    private UserDao userDao;

    @Resource
    private AddressDao addressDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

    @Resource
    private CommonSpecUtil<Second> secondCommonSpecUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询秒杀
     *
     * @param pageable 分页信息
     * @return 分页秒杀
     */
    public Page<SecondWebPageResultVO> findPage(Pageable pageable) {
        Date now = new Date();

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();

        // TODO 缓存优化先不做，后面再做优化

        // 查询数据库，条件：未删除、在结束时间前、没有抢完，按照开始时间正序

        Page<Second> secondPage = secondDao.findAllByEndTimeAfterAndDeletedIsFalse(now, pageable);
        Page<SecondWebPageResultVO> vos = secondPage.map(second -> {
            SecondWebPageResultVO vo = new SecondWebPageResultVO();

            // 商品
            Integer commodityId = second.getCommodityId();
            Commodity commodity = commodityDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
            if (ObjectUtils.isEmpty(commodity)) {
                logger.error("分页查询秒杀失败：商品{} 不存在", commodityId);
                return null;
            }

            // 商品规格
            Integer stockKeepingUnitId = second.getStockKeepingUnitId();
            StockKeepingUnit sku = stockKeepingUnitDao.findOne(stockKeepingUnitId);
            if (ObjectUtils.isEmpty(sku) || sku.getDeleted()) {
                logger.error("分页查询秒杀失败：规格{} 不存在", stockKeepingUnitId);
                return null;
            }

            // 商品轮播，设置商品图片
            List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalseOrderByCommodityBannerIdAsc(commodityId);
            if (ObjectUtils.isEmpty(banners)) {
                logger.error("分页查询秒杀：缺少商品轮播图片");
            } else {
                vo.setCommodityImage(banners.get(0).getCommodityBannerUrl());
            }

//            BeanUtils.copyNonNullProperties(commodity, vo);
//            BeanUtils.copyNonNullProperties(sku, vo);

            BeanUtils.copyNonNullProperties(second, vo);

            String secondPaid = valueOperations.get(RedisKeyPrefix.secondPaid(second.getSecondId()));
            if (!StringUtils.isEmpty(secondPaid)) {
                vo.setCurrentPaidCount(Integer.valueOf(secondPaid));
            }

            return vo;
        });

        return vos;
    }

    /**
     * 查询秒杀详情
     *
     * @param secondId 秒杀id
     * @return 秒杀详情
     */
    public SecondWebDetailVO getDetail(Integer secondId) {

        // TODO 缓存优化先不做，后面再优化

        Second second = secondDao.findOne(secondId);
        if (ObjectUtils.isEmpty(second) || second.getDeleted()) {
            logger.error("查询秒杀详情失败：秒杀{} 不存在", secondId);
            throw new ValidationException(MessageCodes.WEB_SECOND_NOT_EXIST);
        }
        SecondWebDetailVO detailVO = new SecondWebDetailVO();
        BeanUtils.copyNonNullProperties(second, detailVO);

        // 设置商品图片
        Integer commodityId = second.getCommodityId();
        List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        if (!ObjectUtils.isEmpty(banners)) {
            detailVO.setCommodityBanners(banners);
        }

        return detailVO;
    }

    /**
     * 秒杀商品
     *
     * @param userId 用户id
     * @param vo     秒杀id和收货地址id
     * @return 秒杀结果
     */
    public SecondKillResultVO secondKill(Integer userId, SecondKillParamVO vo) {

        // TODO 缓存优化先不做，后面再优化

        Date now = new Date();
        Integer secondId = vo.getSecondId();
        Integer addressId = vo.getAddressId();

        User user = userDao.findOne(userId);
        if (ObjectUtils.isEmpty(user) || user.getDeleted()) {
            logger.error("秒杀失败：用户{} 不存在", userId);
            throw new ValidationException(MessageCodes.WEB_USER_NOT_EXIST);
        }

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Integer secondPaid = Integer.valueOf(valueOperations.get(RedisKeyPrefix.secondPaid(secondId)));
        Integer secondCount = Integer.valueOf(valueOperations.get(RedisKeyPrefix.secondCount(secondId)));

        // 已售完
        if (secondPaid >= secondCount) {
            logger.info("秒杀{} 已经抢完", secondId);
            throw new ValidationException(MessageCodes.WEB_SECOND_SOLD_OUT);
        }

        // 秒杀信息
        Second second = secondDao.findOne(secondId);
        if (ObjectUtils.isEmpty(second) || second.getDeleted()) {
            logger.error("秒杀失败：秒杀{} 不存在", second);
        }

        // 地址信息
        Address address = addressDao.findByAddressIdIsAndDeletedIsFalse(addressId);
        if (ObjectUtils.isEmpty(address)) {
            logger.error("秒杀失败：收货地址{} 不存在");
            throw new ValidationException(MessageCodes.WEB_ADDRESS_NOT_EXIST);
        }

        // TODO 从缓存中取出秒杀信息
        // 创建秒杀订单
        Order order = new Order();
        BeanUtils.copyNonNullProperties(second, order);
        BeanUtils.copyNonNullProperties(address, order);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        order.setOrderType(OrderTypeEnum.ORDER_TYPE_SECOND);
        order.setOrderStatus(OrderStatusEnum.ORDER_WAITING_PAYMENT);
        order.setSecondId(secondId);
        order.setTotalMoney(second.getSecondPrice());
        order.setRealPayMoney(second.getSecondPrice());
        String orderNumber = NumberUtil.generateOrderNumber("sk");
        order.setOrderNumber(orderNumber);
        Order orderSave = orderDao.saveAndFlush(order);

        // 订单条目
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyNonNullProperties(second, orderItem);
        orderItem.setCount(1);
        orderItem.setPrice(second.getSecondPrice());
        orderItem.setOrderId(orderSave.getOrderId());
        orderItemDao.saveAndFlush(orderItem);

        // 构建返回值
        SecondKillResultVO resultVO = new SecondKillResultVO();
        resultVO.setOrderNumber(orderNumber);

        return resultVO;
    }
}
