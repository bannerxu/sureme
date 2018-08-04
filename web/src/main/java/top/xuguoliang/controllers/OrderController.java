package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public OrderWebResultVO createOrder(@RequestBody OrderWebCreateParamVO orderWebCreateParamVO) {
        return orderWebService.createOrder(orderWebCreateParamVO);
    }
}
