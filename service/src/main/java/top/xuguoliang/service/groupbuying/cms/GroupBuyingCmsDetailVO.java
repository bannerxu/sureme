package top.xuguoliang.service.groupbuying.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class GroupBuyingCmsDetailVO {

    @ApiModelProperty("主键id")
    private Integer groupBuyingId;

    // ---------------------拼团信息---------------------

    @ApiModelProperty("拼团价格")
    private BigDecimal groupPrice;

    @ApiModelProperty("拼团人数")
    private Integer peopleNumber;

    @ApiModelProperty("开始时间")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    // ---------------------商品信息---------------------

    @ApiModelProperty("商品id")
    private Integer commodityId;

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
