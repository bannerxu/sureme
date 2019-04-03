package top.banner.service.brokerage.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.banner.models.order.OrderStatusEnum;
import top.banner.models.order.OrderTypeEnum;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class BrokerageVO {
    @ApiModelProperty("佣金记录id")
    private Integer brokerageId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("金额")
    private BigDecimal money;

    @ApiModelProperty("比例")
    private Double scale;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("订单状态")
    private OrderStatusEnum orderStatus;

    @ApiModelProperty("订单类型")
    private OrderTypeEnum orderType;


    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatarUrl;
}
