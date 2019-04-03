package top.banner.service.withdraw.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import top.banner.models.withdraw.WithdrawStatus;

import javax.validation.constraints.NotNull;

/**
 * @description: 提现处理对象
 * @author: XGL
 * @create: 2018-08-20 10:43
 **/
@Data
@NoArgsConstructor
public class WithdrawUpdateVO {
    @ApiModelProperty("提现记录id")
    @NotNull(message = "id不能为空")
    private Integer withdrawId;

    @ApiModelProperty("状态")
    @NotEmpty(message = "状态不能为空")
    private WithdrawStatus withdrawStatus;

    @ApiModelProperty("备注")
    private String remark;

    public WithdrawUpdateVO(@NotNull Integer withdrawId, WithdrawStatus withdrawStatus, String remark) {
        this.withdrawId = withdrawId;
        this.withdrawStatus = withdrawStatus;
        this.remark = remark;
    }
}
