package top.xuguoliang.models.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("文章评论")
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("文章评论id")
    private Integer articleCommentId;

    @ApiModelProperty("发表评论的用户id")
    private Integer userId;

    @ApiModelProperty("文章id")
    private Integer articleId;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

}
