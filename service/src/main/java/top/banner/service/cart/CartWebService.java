package top.banner.service.cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.banner.common.exception.MessageCodes;
import top.banner.common.exception.ValidationException;
import top.banner.common.utils.BeanUtils;
import top.banner.common.utils.CommonSpecUtil;
import top.banner.models.cart.CartItem;
import top.banner.models.cart.CartItemDao;
import top.banner.models.commodity.*;
import top.banner.service.cart.web.CartItemWebResultVO;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinguoguo
 */
@Service
public class CartWebService {

    private static final Logger logger = LoggerFactory.getLogger(CartWebService.class);

    @Resource
    private CartItemDao cartItemDao;

    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private CommodityBannerDao commodityBannerDao;

    @Resource
    private CommonSpecUtil<CartItem> commonSpecUtil;


    /**
     * 通过规格id添加商品到购物车
     *
     * @param userId             用户id
     * @param stockKeepingUnitId 规格Id
     */
    public CartItem addCommodityToCart(Integer userId, Integer stockKeepingUnitId, Integer count) {
        Date date = new Date();
        // 根据用户id和规格id查询购物车条目
        CartItem cartItem = cartItemDao.findByUserIdIsAndStockKeepingUnitIdIsAndDeletedIsFalse(userId, stockKeepingUnitId);
        if (ObjectUtils.isEmpty(cartItem)) {
            // 如果为空则表示当前购物车没有该商品，直接添加
            StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.getOne(stockKeepingUnitId);
            if (ObjectUtils.isEmpty(stockKeepingUnit) || stockKeepingUnit.getDeleted()) {
                logger.error("添加商品到购物车失败：商品规格不存在");
                throw new ValidationException(MessageCodes.WEB_SKU_NOT_EXIST);
            }
            Integer commodityId = stockKeepingUnit.getCommodityId();
            Commodity commodity = commodityDao.getOne(commodityId);
            if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
                logger.error("添加商品到购物车失败：商品不存在");
                throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
            }
            cartItem = new CartItem();
            BeanUtils.copyNonNullProperties(stockKeepingUnit, cartItem);
            BeanUtils.copyNonNullProperties(commodity, cartItem);
            cartItem.setCreateTime(date);
            cartItem.setUpdateTime(date);
            cartItem.setCount(count);
            cartItem.setValid(true);
            cartItem.setUserId(userId);
        } else {
            // 如果不为空则说明已有，添加数量
            cartItem.setCount(cartItem.getCount() + count);
            cartItem.setUpdateTime(date);
            cartItem.setUserId(userId);
        }
        return cartItemDao.saveAndFlush(cartItem);
    }

    /**
     * 判断数量是否小于或等于指定规格的库存
     *
     * @param count              数量
     * @param stockKeepingUnitId 规格id
     * @return 是否小于
     */
    private Boolean isCountLessThanStock(Integer count, Integer stockKeepingUnitId) {
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.getOne(stockKeepingUnitId);
        if (ObjectUtils.isEmpty(stockKeepingUnit) || stockKeepingUnit.getDeleted()) {
            logger.error("判断数量是否小于规格库存失败：规格不存在");
            return false;
        }
        return count <= stockKeepingUnit.getStock();
    }

    /**
     * 从购物车中删除指定的条目
     *
     * @param userId     用户id
     * @param cartItemId 条目id
     * @return 成功与否
     */
    public Boolean deleteCartItem(Integer userId, Integer cartItemId) {
        CartItem cartItem = cartItemDao.findByUserIdIsAndCartItemIdIsAndDeletedIsFalse(userId, cartItemId);
        if (ObjectUtils.isEmpty(cartItem)) {
            logger.error("删除购物车条目失败：条目不存在");
            throw new ValidationException(MessageCodes.WEB_CART_ITEM_NOT_EXIST);
        }
        cartItem.setDeleted(true);
        cartItemDao.saveAndFlush(cartItem);
        return true;
    }

    /**
     * 查询指定用户的所有购物车条目，以更新时间倒序
     *
     * @param userId 用户id
     * @return 购物车条目列表
     */
    public List<CartItemWebResultVO> findAllByUserId(Integer userId) {
        return cartItemDao.findByUserIdIsAndDeletedIsFalseOrderByUpdateTimeDesc(userId)
                .stream()
                .map(cartItem -> {
                    CartItemWebResultVO vo = new CartItemWebResultVO();
                    BeanUtils.copyNonNullProperties(cartItem, vo);
                    Integer commodityId = cartItem.getCommodityId();
                    List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalseOrderByCommodityBannerIdAsc(commodityId);
                    if (ObjectUtils.isEmpty(banners)) {
                        logger.warn("查询购物车条目商品图片失败：商品图片不存在");
                        return vo;
                    }
                    vo.setCommodityUrl(banners.get(0).getCommodityBannerUrl());
                    return vo;
                }).collect(Collectors.toList());
    }

    /**
     * 修改条目数量
     *
     * @param userId     用户id
     * @param cartItemId 条目id
     * @param count      数量
     * @return 成功与否
     */
    public Boolean updateCount(Integer userId, Integer cartItemId, Integer count) {
        CartItem cartItem = cartItemDao.findByUserIdIsAndCartItemIdIsAndDeletedIsFalse(userId, cartItemId);
        if (ObjectUtils.isEmpty(cartItem)) {
            logger.error("修改购物车条目数量失败：条目不存在");
            throw new ValidationException(MessageCodes.WEB_CART_ITEM_NOT_EXIST);
        }
        cartItem.setCount(count);
        cartItemDao.saveAndFlush(cartItem);
        return true;
    }
}
