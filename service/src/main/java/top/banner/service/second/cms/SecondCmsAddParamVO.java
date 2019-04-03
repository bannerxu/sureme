package top.banner.service.second.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class SecondCmsAddParamVO {

    @ApiModelProperty("秒杀数量，库存")
    private Integer secondCount;

    @ApiModelProperty("秒杀价格")
    private BigDecimal secondPrice;

    @ApiModelProperty("开始时间")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

}
