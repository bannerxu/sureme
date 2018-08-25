package top.xuguoliang.service.search.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jinguoguo
 */
@Data
public class SearchOrderItemVO {

    @ApiModelProperty("订单条目id")
    private Integer orderItemId;

    @ApiModelProperty("数量")
    private Integer count;

    @ApiModelProperty("价格")
    private BigDecimal price;

    // ------------------------商品相关------------------------

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("商品简介")
    private String commodityIntroduction;

    @ApiModelProperty("商品图片")
    private String commodityImage;

    @ApiModelProperty("商品详情")
    private String commodityDetail;

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("折扣价")
    private BigDecimal discountPrice;
}
