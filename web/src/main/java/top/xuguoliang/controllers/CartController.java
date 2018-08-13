package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.xuguoliang.models.cart.CartItem;
import top.xuguoliang.service.cart.CartWebService;
import top.xuguoliang.service.cart.web.CartItemWebResultVO;
import top.xuguoliang.service.cart.web.OrderWebCartCreateParamVO;
import top.xuguoliang.service.cart.web.OrderWebCartCreateResultVO;
import top.xuguoliang.service.order.OrderWebService;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
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

    @Resource
    private OrderWebService orderWebService;

    @PostMapping("stockKeepingUnitId/{stockKeepingUnitId}/count/{count}")
    @ApiOperation("添加商品(通过规格)到购物车")
    public CartItem addCommodityToCart(@PathVariable Integer stockKeepingUnitId, @PathVariable @Min(1) Integer count) {
        Integer userId = UserHelper.getUserId();
        return cartWebService.addCommodityToCart(userId, stockKeepingUnitId, count);
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

    @PutMapping("cartItemId/{cartItemId}/count/{count}")
    @ApiOperation("设置数量")
    public Boolean updateCount(@PathVariable Integer cartItemId, @PathVariable @Min(1) Integer count) {
        Integer userId = UserHelper.getUserId();
        return cartWebService.updateCount(userId, cartItemId, count);
    }

    @PostMapping("order")
    @ApiOperation("购物车下单")
    public OrderWebCartCreateResultVO createCartOrder(@RequestBody OrderWebCartCreateParamVO orderWebCartCreateParamVO) {
        Integer userId = UserHelper.getUserId();
        return orderWebService.createCartOrder(userId, orderWebCartCreateParamVO);
    }

}
