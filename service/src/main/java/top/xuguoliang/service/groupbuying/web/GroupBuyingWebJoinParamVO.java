package top.xuguoliang.service.groupbuying.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class GroupBuyingWebJoinParamVO {

    @ApiModelProperty("拼团id")
    private Integer groupBuyingId;

    @ApiModelProperty("收货地址id")
    private Integer addressId;

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

}
