package top.xuguoliang.service.coupon.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.commodity.Commodity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class CouponCmsResultVO {
    @ApiModelProperty("卡券id")
    private Integer couponId;

    @ApiModelProperty("卡券名")
    private String couponName;

    @ApiModelProperty("对应折扣")
    private BigDecimal couponDiscount;

    @ApiModelProperty("关联的商品")
    private List<Commodity> commodities;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
