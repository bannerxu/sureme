package top.xuguoliang.service.cart.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jinguoguo
 */
@Data
public class CartItemWebResultVO {

    @ApiModelProperty("购物车id")
    private Integer cartItemId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("折扣价")
    private BigDecimal discountPrice;

    @ApiModelProperty("数量")
    private Integer count;

    @ApiModelProperty("是否有效")
    private Boolean valid;

    @ApiModelProperty("是否选中")
    private Boolean isSelected;

}
