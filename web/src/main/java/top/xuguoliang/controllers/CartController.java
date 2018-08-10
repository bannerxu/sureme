package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.models.cart.CartItem;
import top.xuguoliang.service.cart.CartWebService;
import top.xuguoliang.service.cart.web.CartItemWebResultVO;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/cart")
@Api(tags = "购物车模块")
public class CartController {

    @Resource
    private CartWebService cartWebService;

    @PostMapping("/{stockKeepingUnitId}")
    @ApiOperation("添加商品(通过规格)到购物车")
    public Boolean addCommodityToCart(@PathVariable Integer stockKeepingUnitId) {
        Integer userId = UserHelper.getUserId();
        return cartWebService.addCommodityToCart(userId, stockKeepingUnitId);
    }

    @DeleteMapping("/{cartItemId}")
    @ApiOperation("删除购物车条目")
    public Boolean deleteCommodityFromCart(@PathVariable Integer cartItemId) {
        Integer userId = UserHelper.getUserId();
        return cartWebService.deleteCartItem(userId, cartItemId);
    }

    @GetMapping
    @ApiOperation("查询所有")
    public List<CartItemWebResultVO> findAllCartItems() {
        Integer userId = UserHelper.getUserId();
        return cartWebService.findAllByUserId(userId);
    }

    @PutMapping("/{cartItemId}/{count}")
    @ApiOperation("计算总价")
    public Boolean updateCount(@PathVariable Integer cartItemId, @PathVariable @Min(1) Integer count) {
        Integer userId = UserHelper.getUserId();
        return cartWebService.updateCount(userId, cartItemId, count);
    }

}
