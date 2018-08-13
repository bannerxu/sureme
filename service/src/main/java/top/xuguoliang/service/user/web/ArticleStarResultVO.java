package top.xuguoliang.service.user.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.article.ArticleTypeEnum;

/**
 * @author jinguoguo
 */
@Data
public class ArticleStarResultVO {

    @ApiModelProperty("主键id")
    private Integer articleStarId;

    @ApiModelProperty("文章id")
    private Integer articleId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("发表文章的管理员id")
    private Integer managerId;

    @ApiModelProperty("文章类型")
    private ArticleTypeEnum articleType;

    @ApiModelProperty("喜欢数")
    private Integer likeCount = 0;

    @ApiModelProperty("文章图片")
    private String articleImage;

}
