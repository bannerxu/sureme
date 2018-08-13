package top.xuguoliang.models.order;

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
@ApiModel("订单条目")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("订单条目id")
    private Integer orderItemId;

    // ------------------------商品相关------------------------

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("折扣价")
    private BigDecimal discountPrice;

    @ApiModelProperty("库存")
    private Integer stock;

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("用户id")
    private Integer userId;

    // --------------------卡券--------------------

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

    @ApiModelProperty("对应的卡券名")
    private String couponName;

}
