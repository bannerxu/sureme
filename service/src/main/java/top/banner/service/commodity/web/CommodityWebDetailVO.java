package top.banner.service.commodity.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.banner.models.comment.CommodityComment;
import top.banner.models.commodity.CommodityBanner;
import top.banner.models.commodity.StockKeepingUnit;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class CommodityWebDetailVO {

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("销量")
    private Integer salesVolume;

    @ApiModelProperty("商品简介")
    private String commodityIntroduction;

    @Lob
    @ApiModelProperty("商品详情")
    private String commodityDetail;

    @ApiModelProperty("商品价格")
    private BigDecimal commodityPrice;

    @ApiModelProperty("分类id")
    private Integer categoryId;

    @ApiModelProperty("分类名")
    private String categoryName;

    @ApiModelProperty("数组：商品评论")
    private List<CommodityComment> comments;

    @ApiModelProperty("数组：商品规格")
    private List<StockKeepingUnit> stockKeepingUnits;

    @ApiModelProperty("数组：商品轮播图")
    private List<CommodityBanner> commodityBanners;

    @ApiModelProperty("数组：卡券")
    private List<CommodityWebCouponVO> coupons;

}
