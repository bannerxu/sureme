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

    @NotNull(message = "文章id不能为空")
    @ApiModelProperty("文章id")
    private Integer articleId;

    @NotBlank(message = "文章标题不能为空")
    @ApiModelProperty("文章标题")
    private String articleTitle;

    @NotBlank(message = "文章内容不能为空")
    @ApiModelProperty("文章内容")
    private String articleContent;

    @NotNull(message = "文章类型不能为空")
    @ApiModelProperty("文章类型")
    private ArticleTypeEnum articleType;

    @ApiModelProperty("数组：文章Banner轮播")
    private List<ArticleBanner> articleBanners;

    @ApiModelProperty("数组：商品")
    private List<Integer> commodityIds;

    @NotNull(message = "怀孕周数不能为空")
    @ApiModelProperty("对应怀孕周数 0通用")
    private Integer pregnancyWeek;

    @ApiModelProperty("宝宝出生天数")
    private Integer babyDay;

    @ApiModelProperty("文章摘要")
    private String articleIntro;
}
