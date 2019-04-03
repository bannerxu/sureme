package top.banner.service.second.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

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
    private String commodityTitle;

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

    @ApiModelProperty("开始时间")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

}
