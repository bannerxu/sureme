package top.xuguoliang.service.second.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author jinguoguo
 */
@Data
public class SecondKillParamVO {

    @NotNull(message = "秒杀id不能为空")
    @ApiModelProperty("秒杀id")
    private Integer secondId;

    @NotNull(message = "收货地址不能为空")
    @ApiModelProperty("收货地址id")
    private Integer addressId;

}
