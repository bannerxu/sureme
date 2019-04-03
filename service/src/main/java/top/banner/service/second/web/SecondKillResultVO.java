package top.banner.service.second.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class SecondKillResultVO {

    @ApiModelProperty("订单编号")
    private String orderNumber;
}
