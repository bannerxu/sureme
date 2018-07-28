package top.xuguoliang.models.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import top.xuguoliang.common.BaseEntity;

import javax.persistence.Entity;

/**
 * @author jinguoguo
 */
@Entity
@ApiModel("商品评论")
public class CommodityComment extends BaseEntity {

    @ApiModelProperty("商品评论id")
    private Integer commodityCommentId;

    @ApiModelProperty("发表评论的用户id")
    private Integer userId;

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("评论内容")
    private String commentContent;

}
