package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.common.constants.RoleConstant;
import top.xuguoliang.service.order.OrderCmsService;
import top.xuguoliang.service.order.cms.OrderCmsPageParamVO;
import top.xuguoliang.service.order.cms.OrderCmsResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/order")
@Api(tags = "订单模块")
public class OrderController {

    @Resource
    private OrderCmsService orderCmsService;

    @GetMapping("page")
    @ApiOperation("分页查询")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public Page<OrderCmsResultVO> findPage(@RequestBody OrderCmsPageParamVO orderCmsPageParamVO) {
        return orderCmsService.findPage(orderCmsPageParamVO);
    }

    @GetMapping("/{orderId}")
    @ApiOperation("单个查询")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public OrderCmsResultVO getOrder(@PathVariable Integer orderId) {
        return orderCmsService.getOrder(orderId);
    }

    @DeleteMapping("/{orderId}")
    @ApiOperation("删除")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public void deleteOrder(@PathVariable Integer orderId) {
        orderCmsService.deleteOrder(orderId);
    }
}
