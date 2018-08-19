package top.xuguoliang.service.withdraw.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.withdraw.WithdrawStatus;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现列表
 */
@Data
public class WithdrawPageVO {

    @ApiModelProperty("提现记录id")
    private Integer withdrawId;

    @ApiModelProperty("提现编号")
    private String code;

    @ApiModelProperty("提现金额")
    private BigDecimal money;

    @ApiModelProperty("提现人")
    private Integer userId;

    @ApiModelProperty("提现人名称")
    private String nickName;

    @ApiModelProperty("创建时间")
    private Date createTime = new Date();

    @ApiModelProperty("状态")
    private WithdrawStatus withdrawStatus;

    @ApiModelProperty("处理时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("对应企业付款订单号")
    private String payCode;
}
