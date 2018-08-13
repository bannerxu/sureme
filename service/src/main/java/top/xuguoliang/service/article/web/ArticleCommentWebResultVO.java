package top.xuguoliang.service.article.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class ArticleCommentWebResultVO {
    @ApiModelProperty("文章评论id")
    private Integer articleCommentId;

    @ApiModelProperty("发表评论的用户id")
    private Integer userId;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("文章id")
    private Integer articleId;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("评论回复")
    private String commentReply;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
