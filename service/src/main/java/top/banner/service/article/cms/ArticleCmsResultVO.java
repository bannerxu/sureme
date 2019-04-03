package top.banner.service.article.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.banner.models.article.ArticleBanner;
import top.banner.models.article.ArticleTypeEnum;
import top.banner.models.commodity.Commodity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class ArticleCmsResultVO {

    @ApiModelProperty("文章id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer articleId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("文章摘要")
    private String articleIntro;

    @ApiModelProperty("文章类型")
    private ArticleTypeEnum articleType;

    @ApiModelProperty("发表文章的管理员id")
    private Integer managerId;

    @ApiModelProperty("发表文章的管理员名")
    private String name;

    @ApiModelProperty("数组：文章banner")
    private List<ArticleBanner> articleBanners;

    @ApiModelProperty("数组：商品")
    private List<Commodity> commodities;

    @ApiModelProperty("对应怀孕周数 0通用")
    private Integer pregnancyWeek;

    @ApiModelProperty("宝宝出生天数")
    private Integer babyDay;
}
