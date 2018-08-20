package top.xuguoliang.service.comment.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author jinguoguo
 */
@Data
public class CmsReplyCommentParamVO {

    @NotBlank(message = "评论回复不能为空")
    @ApiModelProperty("评论回复")
    private String commentReply;

}
