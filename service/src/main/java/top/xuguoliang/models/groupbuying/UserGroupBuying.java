package top.xuguoliang.models.groupbuying;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @ApiModelProperty("对应拼团id")
    private Integer groupBuyingId;

    @ApiModelProperty("对应商品id")
    private Integer commodityId;

    @ApiModelProperty("商品名称")
    private String commodityName;

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
