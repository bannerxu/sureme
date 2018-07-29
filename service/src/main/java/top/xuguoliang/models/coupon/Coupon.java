package top.xuguoliang.models.coupon;

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
@ApiModel("卡券")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("卡券id")
    private Integer couponId;

    @ApiModelProperty("卡券名")
    private String couponName;

    @ApiModelProperty("商品id，为零全适用，非零针对单一商品")
    private Integer commodityId;

    @ApiModelProperty("用户id，为零全适用，非零针对单一用户")
    private Integer userId;

    @ApiModelProperty("对应折扣")
    private BigDecimal couponDiscount;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

}
