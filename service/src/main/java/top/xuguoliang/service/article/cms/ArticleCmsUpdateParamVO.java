package top.xuguoliang.service.article.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import top.xuguoliang.models.article.ArticleBanner;
import top.xuguoliang.models.article.ArticleTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class ArticleCmsUpdateParamVO {

    @NotNull
    @ApiModelProperty("文章id")
    private Integer articleId;

    @NotBlank
    @ApiModelProperty("文章标题")
    private String articleTitle;

    @NotBlank
    @ApiModelProperty("文章内容")
    private String articleContent;

    @NotNull
    @ApiModelProperty("文章类型")
    private ArticleTypeEnum articleType;

    @NotNull
    @ApiModelProperty("发表文章的管理员id")
    private Integer managerId;

    @ApiModelProperty("数组：文章Banner轮播")
    private List<ArticleBanner> articleBanners;

    @ApiModelProperty("数组：商品")
    private List<Integer> commodityIds;

}
