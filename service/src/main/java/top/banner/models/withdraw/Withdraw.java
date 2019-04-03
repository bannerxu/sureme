package top.banner.models.withdraw;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 佣金提现记录
 *
 * @author XGL
 */
@Data
@Entity
@NoArgsConstructor
public class Withdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("提现记录id")
    private Integer withdrawId;

    @ApiModelProperty("提现编号")
    private String code;

    @ApiModelProperty("提现金额")
    private BigDecimal money;

    @ApiModelProperty("提现人")
    private Integer userId;

    @ApiModelProperty("创建时间")
    private Date createTime = new Date();

    @ApiModelProperty("状态")
    private WithdrawStatus withdrawStatus = WithdrawStatus.WAIT;

    @ApiModelProperty("处理时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("对应企业付款订单号")
    private String payCode;

    @ApiModelProperty("是否删除")
    private Boolean deleted = false;


    public Withdraw(String code, BigDecimal money, Integer userId) {
        this.code = code;
        this.money = money;
        this.userId = userId;
    }
}
