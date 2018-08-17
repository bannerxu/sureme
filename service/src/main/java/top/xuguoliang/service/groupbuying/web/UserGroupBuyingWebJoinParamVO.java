package top.xuguoliang.service.groupbuying.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class UserGroupBuyingWebJoinParamVO {

    @ApiModelProperty("拼团id")
    private Integer userGroupBuyingId;

    @ApiModelProperty("收货地址id")
    private Integer addressId;

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

}
