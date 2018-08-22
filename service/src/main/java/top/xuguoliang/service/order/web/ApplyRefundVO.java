package top.xuguoliang.service.order.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.apply.ApplyType;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class ApplyRefundVO {

    @NotNull(message = "订单id不能为空")
    @ApiModelProperty("订单id")
    private Integer orderId;

    @NotNull(message = "申请类型不能为空")
    @ApiModelProperty("申请类型")
    private ApplyType applyType;

    @NotNull(message = "原因不能为空")
    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("图片")
    private List<String> images;

}
