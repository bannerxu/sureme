package top.xuguoliang.service.article.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author jinguoguo
 */
@Data
@ApiModel("文章评论")
public class ArticleCommentWebAddVO {

    @NotBlank(message = "评论内容不能为空")
    @ApiModelProperty("文章评论")
    private String commentContent;
}
