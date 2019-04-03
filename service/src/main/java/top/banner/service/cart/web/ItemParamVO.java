package top.banner.service.cart.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class ItemParamVO {
    @ApiModelProperty("规格")
    private Integer stockKeepingUnitId;

    @ApiModelProperty("数量")
    private Integer skuCount;
}
