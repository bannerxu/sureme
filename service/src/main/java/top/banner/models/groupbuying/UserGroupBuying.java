package top.banner.models.groupbuying;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("用户拼团")
public class UserGroupBuying {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("用户拼团id")
    private Integer userGroupBuyingId;

    @ApiModelProperty("发起者的用户id")
    private Integer sponsorUserId;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("对应拼团id")
    private Integer groupBuyingId;

    @ApiModelProperty("对应商品id")
    private Integer commodityId;

    @ApiModelProperty("商品名称")
    private String commodityTitle;

    @Lob
    @ApiModelProperty("商品详情")
    private String commodityDetail;

    @ApiModelProperty("销量")
    private Integer salesVolume;

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("原价")
    private BigDecimal originalPrice;

    @ApiModelProperty("团购价")
    private BigDecimal groupPrice;

    @ApiModelProperty("最大人数")
    private Integer maxPeopleNumber;

    @ApiModelProperty("当前人数")
    private Integer currentPeopleNumber;

    @ApiModelProperty("是否满人")
    private Boolean isFull;

    @ApiModelProperty("开始时间")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
