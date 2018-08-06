package top.xuguoliang.service.commodity.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import top.xuguoliang.models.commodity.CommodityBanner;
import top.xuguoliang.models.commodity.StockKeepingUnit;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class CommodityCmsAddParamVO {

    @NotBlank(message = "商品标题不能为空")
    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @NotBlank(message = "商品详情不能为空")
    @ApiModelProperty("商品详情")
    private String commodityDetail;

    @NotBlank(message = "商品简介不能为空")
    @ApiModelProperty("商品简介")
    private String commodityIntroduction;

    @NotNull(message = "商品价格不能为空")
    @ApiModelProperty("商品价格")
    private BigDecimal commodityPrice;

    @ApiModelProperty("数组：商品轮播图")
    private List<CommodityBanner> commodityBanners;

    @NotNull(message = "商品规格不能为空，至少添加一个")
    @ApiModelProperty("数组：商品规格")
    private List<StockKeepingUnit> stockKeepingUnits;

    @NotNull(message = "分类不能为空")
    @ApiModelProperty("分类id")
    private Integer categoryId;

}
