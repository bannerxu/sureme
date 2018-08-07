package top.xuguoliang.service.comment.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;

/**
 * @author jinguoguo
 */
@Data
public class ArticleCommentCmsResultVO {

    @ApiModelProperty("文章评论id")
    private Integer articleCommentId;

    @ApiModelProperty("发表评论的用户id")
    private Integer userId;

    @ApiModelProperty("文章id")
    private Integer articleId;

    @Lob
    @ApiModelProperty("评论内容")
    private String commentContent;

    @Lob
    @ApiModelProperty("评论回复")
    private String commentReply;

}
