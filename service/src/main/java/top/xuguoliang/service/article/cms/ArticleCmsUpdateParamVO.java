package top.xuguoliang.service.article.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.article.Article;
import top.xuguoliang.models.article.ArticleBanner;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class ArticleCmsUpdateParamVO {

    @Id
    @ApiModelProperty("文章id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer articleId;

    @ApiModelProperty("文章轮播图url")
    private String articleBannerUrl;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("发表文章的用户id")
    private Integer userId;

    @ApiModelProperty("数组：文章Banner轮播")
    private List<ArticleBanner> articleBanners;

}
