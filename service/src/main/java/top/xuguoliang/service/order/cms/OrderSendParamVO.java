package top.xuguoliang.service.order.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class OrderSendParamVO {

    @ApiModelProperty("物流编号")
    private String logisticsNumber;


}
