package top.xuguoliang.service.article.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.article.ArticleBanner;
import top.xuguoliang.models.article.ArticleTypeEnum;
import top.xuguoliang.models.comment.ArticleComment;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class ArticleWebDetailVO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer articleId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("文章类型")
    private ArticleTypeEnum articleType;

    @ApiModelProperty("发表文章的管理员id")
    private Integer managerId;

    @ApiModelProperty("发表文章的管理员名称")
    private String managerName;

    @ApiModelProperty("喜欢数")
    private Integer likeCount = 0;

    @ApiModelProperty("收藏数")
    private Integer starCount = 0;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("关联商品")
    private List<ArticleWebCommodityVO> commodities;

    @ApiModelProperty("文章轮播")
    private List<ArticleBanner> articleBanners;

    @ApiModelProperty("喜欢")
    private Boolean isLike = false;

    @ApiModelProperty("收藏")
    private Boolean isStar = false;

    @ApiModelProperty("对应怀孕周数 0通用")
    private Integer pregnancyWeek;

    @ApiModelProperty("宝宝出生天数")
    private Integer babyDay;
}
