package top.banner.service.groupbuying.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jinguoguo
 */
@Data
public class GroupBuyingWebResultVO {

    @ApiModelProperty("主键id")
    private Integer groupBuyingId;

    @ApiModelProperty("商品图片")
    private String commodityImage;

    @ApiModelProperty("商品名称")
    private String commodityName;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("原价")
    private BigDecimal originalPrice;

    @ApiModelProperty("团购价")
    private BigDecimal groupPrice;

    @ApiModelProperty("总人数")
    private Integer maxPeopleNumber;

}
