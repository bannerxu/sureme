package top.xuguoliang.models.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author jinguoguo
 */
@Entity
@ApiModel("文章评论")
public class ArticleComment extends BaseEntity {

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

}
