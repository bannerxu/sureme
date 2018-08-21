package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.common.constants.RoleConstant;
import top.xuguoliang.models.order.OrderStatusEnum;
import top.xuguoliang.service.order.OrderCmsService;
import top.xuguoliang.service.order.cms.OrderCmsResultVO;
import top.xuguoliang.service.order.cms.OrderSendParamVO;

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
    public Page<OrderCmsResultVO> findPage(@RequestParam(required = false) OrderStatusEnum orderStatus,
                                           @RequestParam(required = false) @PageableDefault Pageable pageable) {
        return orderCmsService.findPage(orderStatus, pageable);
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

    @PutMapping("/{orderId}")
    @ApiOperation("发货")
    public void send(@PathVariable Integer orderId, @RequestBody OrderSendParamVO vo) {
        orderCmsService.send(orderId, vo);
    }

    @GetMapping("logistics/{orderId}")
    @ApiOperation("获取物流")
    public String getLogisticsInfo(@PathVariable Integer orderId) {
        return orderCmsService.getLogisticsInfo(orderId);
    }
}
