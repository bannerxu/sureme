package top.xuguoliang.service.search.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.article.ArticleTypeEnum;

/**
 * @author jinguoguo
 */
@Data
public class SearchArticleResultVO {

    @ApiModelProperty("文章id")
    private Integer articleId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("文章类型")
    private ArticleTypeEnum articleType;

    @ApiModelProperty("发表文章的管理员id")
    private Integer managerId;

    @ApiModelProperty("管理员名称")
    private String managerName;

    @ApiModelProperty("喜欢数")
    private Integer likeCount = 0;

    @ApiModelProperty("收藏数")
    private Integer starCount = 0;

    @ApiModelProperty("喜欢")
    private Boolean isLike = false;

    @ApiModelProperty("收藏")
    private Boolean isStar = false;

    @ApiModelProperty("文章图片")
    private String articleImage;

    @ApiModelProperty("对应怀孕周数 0通用")
    private Integer pregnancyWeek;

}
