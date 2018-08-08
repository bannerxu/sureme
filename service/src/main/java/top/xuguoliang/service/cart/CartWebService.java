package top.xuguoliang.service.cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.cart.CartItem;
import top.xuguoliang.models.cart.CartItemDao;
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.commodity.StockKeepingUnit;
import top.xuguoliang.models.commodity.StockKeepingUnitDao;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    private CommonSpecUtil<CartItem> commonSpecUtil;


    /**
     * 通过规格id添加商品到购物车
     *
     * @param userId             用户id
     * @param stockKeepingUnitId 规格Id
     */
    public Boolean addCommodityToCart(Integer userId, Integer stockKeepingUnitId) {
        Date date = new Date();
        // 根据用户id和规格id查询购物车条目
        List<CartItem> cartItems = cartItemDao.findByUserIdIsAndStockKeepingUnitIdIsAndDeletedIsFalse(userId, stockKeepingUnitId);
        if (ObjectUtils.isEmpty(cartItems)) {
            // 如果为空则表示当前购物车没有该商品
            StockKeepingUnit stockKeepingUnit = stockKeepingUnitDao.findOne(stockKeepingUnitId);
            if (ObjectUtils.isEmpty(stockKeepingUnit) || stockKeepingUnit.getDeleted()) {
                logger.error("添加商品到购物车失败：商品规格不存在");
                throw new ValidationException(MessageCodes.WEB_SKU_NOT_EXIST);
            }
            Integer commodityId = stockKeepingUnit.getCommodityId();
            Commodity commodity = commodityDao.findOne(commodityId);
            if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
                logger.error("添加商品到购物车失败：商品不存在");
                throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
            }
            CartItem cartItem = new CartItem();
            BeanUtils.copyNonNullProperties(stockKeepingUnit, cartItem);
            BeanUtils.copyNonNullProperties(commodity, cartItem);
            cartItem.setCreateTime(date);
            cartItem.setUpdateTime(date);
            cartItem.setCount(1);
            cartItem.setValid(true);
            cartItemDao.saveAndFlush(cartItem);
            return true;
        } else {
            // 如果不为空则说明已有，直接添加数量
            cartItems.forEach(cartItem -> {
                if (cartItem.getStockKeepingUnitId().equals(stockKeepingUnitId)) {
                    cartItem.setCount(cartItem.getCount() + 1);
                    cartItem.setUpdateTime(date);
                    cartItemDao.saveAndFlush(cartItem);
                }
            });
            return true;
        }
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
    public List<CartItem> findAllByUserId(Integer userId) {
        return cartItemDao.findByUserIdIsAndDeletedIsFalseOrderByUpdateTimeDesc(userId);
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
        if(ObjectUtils.isEmpty(cartItem)) {
            logger.error("修改购物车条目数量失败：条目不存在");
            throw new ValidationException(MessageCodes.WEB_CART_ITEM_NOT_EXIST);
        }
        cartItem.setCount(count);
        cartItemDao.saveAndFlush(cartItem);
        return true;
    }
}
