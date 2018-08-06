package top.xuguoliang.service.commodity.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.commodity.CommodityBanner;
import top.xuguoliang.models.commodity.StockKeepingUnit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class CommodityCmsResultVO {

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("销量")
    private Integer salesVolume;

    @ApiModelProperty("商品详情")
    private String commodityDetail;

    @ApiModelProperty("商品价格")
    private BigDecimal commodityPrice;

    @ApiModelProperty("数组：商品轮播")
    private List<CommodityBanner> commodityBanners;

    @ApiModelProperty("商品规格")
    private List<StockKeepingUnit> stockKeepingUnits;

    @ApiModelProperty("分类id")
    private Integer categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

}
