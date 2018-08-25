package top.xuguoliang.service.search.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.order.OrderStatusEnum;
import top.xuguoliang.models.order.OrderTypeEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class SearchOrderResultVO {
    // 订单编号	订单类型	商品图片	商品/规格名称	规格单价/折扣价	数量	订单总金额	实际支付金额	订单状态
    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("订单状态")
    private OrderStatusEnum orderStatus;

    @ApiModelProperty("订单类型")
    private OrderTypeEnum orderType;

    @ApiModelProperty("订单条目")
    private List<SearchOrderItemVO> orderItems;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalMoney;

    @ApiModelProperty("实际支付金额")
    private BigDecimal realPayMoney;

}
