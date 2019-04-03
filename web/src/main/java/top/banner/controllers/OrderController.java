package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.models.order.OrderStatusEnum;
import top.banner.service.comment.web.CommentOrderParamVO;
import top.banner.service.order.OrderWebService;
import top.banner.service.order.web.ApplyRefundVO;
import top.banner.service.order.web.OrderWebDetailVO;
import top.banner.service.order.web.OrderWebResultVO;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

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
    public Page<OrderWebResultVO> findPage(@PathVariable OrderStatusEnum orderStatus,
                                           @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Integer userId = UserHelper.getUserId();
        return orderWebService.findPage(userId, orderStatus, pageable);
    }

    @GetMapping("/{orderId}")
    @ApiOperation("订单详情")
    public OrderWebDetailVO getDetail(@PathVariable Integer orderId) {
        Integer userId = UserHelper.getUserId();
        return orderWebService.getDetail(userId, orderId);
    }

    @DeleteMapping("/{orderId}")
    @ApiOperation("取消|删除 订单")
    public void cancelOrder(@PathVariable Integer orderId) {
        Integer userId = UserHelper.getUserId();
        orderWebService.deleteOrder(userId, orderId);
    }


    @ApiOperation("订单评价")
    @PostMapping("comment")
    public void commentOrder(@RequestBody CommentOrderParamVO vo) {
        Integer userId = UserHelper.getUserId();
        orderWebService.commentOrder(userId, vo);
    }

    @PutMapping("receive/{orderId}")
    @ApiOperation("确认收货")
    public void received(@PathVariable @NotNull(message = "必须指定订单") Integer orderId) {
        orderWebService.received(UserHelper.getUserId(), orderId);
    }

    @GetMapping("logistics/{orderId}")
    @ApiOperation("获取物流")
    public String getLogisticsInfo(@PathVariable Integer orderId) {
        return orderWebService.getLogisticsInfo(orderId);
    }

    @PostMapping
    @ApiOperation("申请退款")
    public void applyRefund(@RequestBody ApplyRefundVO applyRefundVO) {
        Integer userId = UserHelper.getUserId();
        orderWebService.applyRefund(userId, applyRefundVO);
    }
}
