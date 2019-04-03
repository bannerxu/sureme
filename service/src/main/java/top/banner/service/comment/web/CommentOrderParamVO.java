package top.banner.service.comment.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class CommentOrderParamVO {

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("评论内容")
    private String commentContent;

}
