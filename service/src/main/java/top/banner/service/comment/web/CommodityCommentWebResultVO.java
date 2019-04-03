package top.banner.service.comment.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class CommodityCommentWebResultVO {
    @ApiModelProperty("商品评论id")
    private Integer commodityCommentId;

    @ApiModelProperty("发表评论的用户id")
    private Integer userId;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("评论回复")
    private String commentReply;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
