package top.banner.service.article.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author jinguoguo
 */
@Data
public class ArticleCmsDeleteRelationVO {

    @NotNull(message = "删除商品时文章id不能为空")
    @ApiModelProperty("文章id")
    private Integer articleId;

    @NotNull(message = "删除商品是商品id不能为空")
    @ApiModelProperty("商品id")
    private Integer commodityId;
}
