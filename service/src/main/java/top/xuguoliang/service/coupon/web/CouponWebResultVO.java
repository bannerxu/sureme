package top.xuguoliang.service.coupon.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.coupon.CouponTypeEnum;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class CouponWebResultVO {
    @ApiModelProperty("卡券id")
    private Integer couponId;

    @ApiModelProperty("卡券名")
    private String couponName;

    @ApiModelProperty("卡券类型")
    private CouponTypeEnum couponType;

    @ApiModelProperty("对应折扣")
    private BigDecimal couponDiscount;

    @ApiModelProperty("满额")
    private BigDecimal minUseMoney;

    @ApiModelProperty("减额")
    private BigDecimal offsetMoney;

    @ApiModelProperty("领取开始时间")
    private Date pullBeginTime;

    @ApiModelProperty("领取结束时间")
    private Date pullEndTime;

    @ApiModelProperty("使用开始时间")
    private Date useBeginTime;

    @ApiModelProperty("使用结束时间")
    private Date useEndTime;

    @ApiModelProperty("是否已经领取")
    private Boolean isPulled = false;
}
