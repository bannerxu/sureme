package top.banner.service.second.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.banner.models.commodity.CommodityBanner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class SecondWebDetailVO {

    @ApiModelProperty("秒杀id")
    private Integer secondId;

    @ApiModelProperty("秒杀数量，库存")
    private Integer secondCount;

    @ApiModelProperty("秒杀价格")
    private BigDecimal secondPrice;

    @ApiModelProperty("原价")
    private BigDecimal originalPrice;

    @ApiModelProperty("开始时间")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    private List<CommodityBanner> commodityBanners;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

    // ---------------------商品信息---------------------

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("商品名称")
    private String commodityName;

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("销量")
    private Integer salesVolume;

    @ApiModelProperty("商品简介")
    private String commodityIntroduction;

    @ApiModelProperty("商品详情")
    private String commodityDetail;

    @ApiModelProperty("商品价格")
    private BigDecimal commodityPrice;

    @ApiModelProperty("分类id")
    private Integer categoryId;

    @ApiModelProperty("分类名")
    private String categoryName;

    // ---------------------规格信息---------------------

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("折扣价")
    private BigDecimal discountPrice;

    @ApiModelProperty("库存")
    private Integer stock;

}
