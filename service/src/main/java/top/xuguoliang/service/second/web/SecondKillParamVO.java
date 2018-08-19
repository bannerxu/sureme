package top.xuguoliang.service.second.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class SecondKillParamVO {

    @ApiModelProperty("秒杀id")
    private Integer secondId;

    @ApiModelProperty("收货地址id")
    private Integer addressId;

}
