package top.xuguoliang.service.banner.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.banner.BannerTypeEnum;

/**
 * @author jinguoguo
 */
@Data
public class BannerCmsAddParamVO {

    @ApiModelProperty("轮播id")
    private Integer bannerId;

    @ApiModelProperty("轮播类型")
    private BannerTypeEnum bannerType;

    @ApiModelProperty("对应的文章id")
    private Integer articleId;

    @ApiModelProperty("对应的商品id")
    private Integer commodityId;

    @ApiModelProperty("对应的文章轮播id")
    private Integer articleBannerId;

    @ApiModelProperty("文章轮播url")
    private String articleBannerUrl;

    @ApiModelProperty("对应的商品轮播id")
    private Integer commodityBannerId;

    @ApiModelProperty("商品轮播url")
    private String commodityBannerUrl;
}
