package top.xuguoliang.models.brokerage;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@ApiModel("佣金记录")
public class Brokerage {

    @ApiModelProperty("佣金记录id")
    private Integer brokerageId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("订单号")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("金额")
    private BigDecimal money;

    @ApiModelProperty("比例")
    private Double scale;

    @ApiModelProperty("创建时间")
    private Date createTime = new Date();
}
