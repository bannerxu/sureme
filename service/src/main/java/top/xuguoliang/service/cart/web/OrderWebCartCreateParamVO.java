package top.xuguoliang.service.cart.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.commodity.StockKeepingUnit;

import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class OrderWebCartCreateParamVO {

    @ApiModelProperty("规格列表")
    private List<StockKeepingUnit> stockKeepingUnits;

    @ApiModelProperty("地址id")
    private Integer addressId;

    @ApiModelProperty("个人卡券id")
    private Integer personalCouponId;

}
