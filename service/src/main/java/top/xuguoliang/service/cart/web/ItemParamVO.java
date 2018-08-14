package top.xuguoliang.service.cart.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.commodity.StockKeepingUnit;

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
