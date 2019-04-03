package top.banner.service.article.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.banner.models.commodity.CommodityBanner;

/**
 * @author jinguoguo
 */
@Data
public class ArticleWebCommodityVO {

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("商品轮播")
    private CommodityBanner commodityBanner;
}
