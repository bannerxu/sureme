package top.banner.models.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("文章")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("文章id")
    private Integer articleId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @Lob
    @ApiModelProperty("文章内容")
    private String articleContent;

    @Lob
    @ApiModelProperty("文章摘要")
    private String articleIntro;

    @ApiModelProperty("文章类型")
    private ArticleTypeEnum articleType;

    @ApiModelProperty("发表文章的管理员id")
    private Integer managerId;

    @ApiModelProperty("喜欢数")
    private Integer likeCount = 0;

    @ApiModelProperty("收藏数")
    private Integer starCount = 0;

    @ApiModelProperty("对应怀孕周数 0通用")
    private Integer pregnancyWeek;

    @ApiModelProperty("宝宝出生天数")
    private Integer babyDay;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

}
