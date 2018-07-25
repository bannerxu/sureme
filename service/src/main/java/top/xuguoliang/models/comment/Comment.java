package top.xuguoliang.models.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@ApiModel("评论")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("评论id")
    private Integer commentId;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("评论类型")
    private CommentEnum commentType;

    @ApiModelProperty("对应商品id，与文章id互斥")
    private Integer commodityId;

    @ApiModelProperty("对应文章id，与商品id互斥")
    private Integer articleId;

    @ApiModelProperty("发表评论的用户id")
    private Integer userId;
}
