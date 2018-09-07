package top.xuguoliang.service.apply.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.apply.ApplyStatus;
import top.xuguoliang.models.apply.ApplyType;

import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class ApplyRecordVO {

    @ApiModelProperty("申请记录id")
    private Integer applyRecordId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("原因")
    private String applyReason;

    @ApiModelProperty("申请类型")
    private ApplyType applyType;

    @ApiModelProperty("申请状态")
    private ApplyStatus applyStatus;

    @ApiModelProperty("申请时间")
    private Date applyTime;

}
