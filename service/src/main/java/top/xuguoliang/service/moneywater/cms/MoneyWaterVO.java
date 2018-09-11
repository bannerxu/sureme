package top.xuguoliang.service.moneywater.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.moneywater.MoneyWaterType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class MoneyWaterVO {
    @ApiModelProperty("资金流水id")
    private Integer moneyWaterId;

    @ApiModelProperty("类型：PAY | REFUND | WITHDRAW")
    private MoneyWaterType type;

    @ApiModelProperty("金额")
    private BigDecimal money;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("时间")
    private Date time;

    @ApiModelProperty("删除")
    private Boolean deleted = false;
}
