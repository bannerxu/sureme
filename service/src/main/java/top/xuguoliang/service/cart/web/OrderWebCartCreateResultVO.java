package top.xuguoliang.service.cart.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class OrderWebCartCreateResultVO {

    @ApiModelProperty("订单编号")
    private String orderNumber;
}
