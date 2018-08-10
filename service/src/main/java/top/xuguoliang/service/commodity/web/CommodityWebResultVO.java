package top.xuguoliang.service.commodity.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.comment.CommodityComment;
import top.xuguoliang.models.commodity.CommodityBanner;
import top.xuguoliang.models.commodity.StockKeepingUnit;
import top.xuguoliang.models.coupon.Coupon;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class CommodityWebResultVO {

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("销量")
    private Integer salesVolume;

    @ApiModelProperty("商品简介")
    private String commodityIntroduction;

}
