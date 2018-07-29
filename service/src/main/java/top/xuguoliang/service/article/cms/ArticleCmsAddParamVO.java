package top.xuguoliang.service.article.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.article.ArticleBanner;

import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class ArticleCmsAddParamVO {

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("数组：文章轮播图url")
    private List<String> articleBanners;

    @ApiModelProperty("数组：商品id")
    private List<Integer> commodityIds;
}
