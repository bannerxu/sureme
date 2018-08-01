package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.common.constants.RoleConstant;
import top.xuguoliang.service.order.cms.OrderCmsResultVO;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/order")
@Api(tags = "订单模块")
public class OrderController {

    @GetMapping("page")
    @ApiOperation("分页查询")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public OrderCmsResultVO findPage() {
        return null;
    }

    @DeleteMapping("/{orderId}")
    @ApiOperation("删除")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ADMIN, RoleConstant.ROOT})
    public void deleteOrder(@PathVariable Integer orderId) {

    }
}
