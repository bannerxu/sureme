package top.xuguoliang.service.groupbuying.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jinguoguo
 */
@Data
public class UserGroupBuyingWebResultVO {

    @ApiModelProperty("用户拼团id")
    private Integer userGroupBuyingId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("商品图片")
    private String commodityImage;

    @ApiModelProperty("商品名称")
    private String commodityName;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("原价")
    private BigDecimal originalPrice;

    @ApiModelProperty("团购价")
    private BigDecimal groupPrice;

    @ApiModelProperty("差人数")
    private Integer needPeopleNumber;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("用户昵称")
    private String nickname;

}
