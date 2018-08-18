package top.xuguoliang.service.second.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jinguoguo
 */
@Data
public class SecondWebPageResultVO {

    @ApiModelProperty("秒杀id")
    private Integer secondId;

    @ApiModelProperty("商品图片")
    private String commodityImage;

    @ApiModelProperty("商品名称")
    private String commodityName;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("原价")
    private BigDecimal originalPrice;

    @ApiModelProperty("秒杀价")
    private BigDecimal secondPrice;

    @ApiModelProperty("总库存")
    private Integer secondCount;

    @ApiModelProperty("目前已买")
    private Integer currentPaidCount;

}
