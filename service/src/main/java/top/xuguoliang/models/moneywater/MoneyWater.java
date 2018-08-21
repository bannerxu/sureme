package top.xuguoliang.models.moneywater;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("资金流水")
public class MoneyWater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ApiModelProperty("时间")
    private Date time;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

}
