package top.banner.service.payment.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author jinguoguo
 */
@Data
public class UnifiedOrderParam {

    @NotNull(message = "订单id不能为空")
    @ApiModelProperty("订单id")
    private Integer orderId;

}
