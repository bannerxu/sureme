package top.banner.models.comment;

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
@ApiModel("文章评论")
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("文章评论id")
    private Integer articleCommentId;

    @ApiModelProperty("发表评论的用户id")
    private Integer userId;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("文章id")
    private Integer articleId;

    @Lob
    @ApiModelProperty("评论内容")
    private String commentContent;

    @Lob
    @ApiModelProperty("评论回复")
    private String commentReply;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

}
