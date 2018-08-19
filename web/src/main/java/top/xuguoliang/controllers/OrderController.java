package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.models.order.OrderStatusEnum;
import top.xuguoliang.service.order.OrderWebService;
import top.xuguoliang.service.order.web.OrderWebCreateParamVO;
import top.xuguoliang.service.order.web.OrderWebDetailVO;
import top.xuguoliang.service.order.web.OrderWebResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/order")
@Api(tags = "订单模块")
public class OrderController {

    @Resource
    private OrderWebService orderWebService;

    @GetMapping("status/{orderStatus}")
    @ApiOperation("分页查询")
    public Page<OrderWebResultVO> findPage(@PathVariable OrderStatusEnum orderStatus, @PageableDefault Pageable pageable) {
        Integer userId = UserHelper.getUserId();
        return orderWebService.findPage(userId, pageable);
    }

    @GetMapping("/{orderId}")
    @ApiOperation("订单详情")
    public OrderWebDetailVO getDetail(@PathVariable Integer orderId) {
        Integer userId = UserHelper.getUserId();
        return orderWebService.getDetail(userId, orderId);
    }

    @ApiOperation("下单")
    @PostMapping
    public OrderWebResultVO createOrder(@RequestBody OrderWebCreateParamVO orderWebCreateParamVO) {
        Integer userId = UserHelper.getUserId();
        return orderWebService.createOrder(userId, orderWebCreateParamVO);
    }

    @ApiOperation("通过订单号查询订单")
    @GetMapping("/{orderNumber}")
    public OrderWebResultVO findByOrderNumber(@PathVariable String orderNumber) {
        return orderWebService.findByOrderNumber(orderNumber);
    }
}
