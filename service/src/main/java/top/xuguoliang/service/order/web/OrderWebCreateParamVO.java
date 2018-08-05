package top.xuguoliang.service.order.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.order.OrderStatusEnum;
import top.xuguoliang.models.order.OrderTypeEnum;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class OrderWebCreateParamVO {

    @NotNull(message = "订单类型不能为空")
    @ApiModelProperty("订单类型")
    private OrderTypeEnum orderType;

    @NotNull(message = "商品不能为空")
    @ApiModelProperty("商品id")
    private Integer commodityId;

    @NotNull(message = "收货地址不能为空")
    @ApiModelProperty("收货地址id")
    private Integer addressId;

    @NotNull(message = "商品规格不能为空")
    @ApiModelProperty("商品规格id")
    private Integer stockKeepingUnitId;

}
