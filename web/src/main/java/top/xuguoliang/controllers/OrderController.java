package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.order.OrderWebService;
import top.xuguoliang.service.order.web.OrderWebCreateParamVO;
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
