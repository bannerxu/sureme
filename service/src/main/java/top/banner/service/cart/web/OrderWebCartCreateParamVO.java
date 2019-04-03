package top.banner.service.cart.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class OrderWebCartCreateParamVO {

    @ApiModelProperty("items")
    private List<ItemParamVO> items;

    @ApiModelProperty("地址id")
    private Integer addressId;

    @ApiModelProperty("个人卡券id")
    private Integer personalCouponId;

}
