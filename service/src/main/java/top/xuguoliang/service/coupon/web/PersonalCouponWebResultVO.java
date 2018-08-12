package top.xuguoliang.service.coupon.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class PersonalCouponWebResultVO {

    @ApiModelProperty("个人卡券id")
    private Integer personalCouponId;

    @ApiModelProperty("对应的卡券id")
    private Integer couponId;

    @ApiModelProperty("满额")
    private BigDecimal minUseMoney;

    @ApiModelProperty("减额")
    private BigDecimal offsetMoney;

    @ApiModelProperty("使用开始时间")
    private Date useBeginTime;

    @ApiModelProperty("使用结束时间")
    private Date useEndTime;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("对应的卡券名")
    private String couponName;

    @ApiModelProperty("是否有效")
    private Boolean valid;

}
