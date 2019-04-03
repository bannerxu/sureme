package top.banner.service.search.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.banner.models.article.ArticleTypeEnum;

import java.util.Date;
import java.util.List;

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

    @ApiModelProperty("关联商品")
    private List<SearchArticleCommodityVO> commodities;

    @ApiModelProperty("发表文章的管理员id")
    private Integer managerId;

    @ApiModelProperty("发表文章的管理员姓名")
    private String managerName;

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

}
