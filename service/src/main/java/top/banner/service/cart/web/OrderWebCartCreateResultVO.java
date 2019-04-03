package top.banner.service.cart.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class OrderWebCartCreateResultVO {

    @ApiModelProperty("订单id")
    private Integer orderId;
}
