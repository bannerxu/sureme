package top.banner.service.order.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class OrderSendParamVO {

    @ApiModelProperty("物流编号")
    private String logisticsNumber;

    @ApiModelProperty("物流公司代码")
    private String logisticsCompany;

}
