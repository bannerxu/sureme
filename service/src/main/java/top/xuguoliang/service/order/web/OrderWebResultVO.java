package top.xuguoliang.service.order.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.order.OrderItem;
import top.xuguoliang.models.order.OrderStatusEnum;
import top.xuguoliang.models.order.OrderTypeEnum;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class OrderWebResultVO {

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("订单状态")
    private OrderStatusEnum orderStatus;

    @ApiModelProperty("订单类型")
    private OrderTypeEnum orderType;

    @ApiModelProperty("秒杀id，只在订单类型是秒杀时有用")
    private Integer secondId;

    @ApiModelProperty("拼团id，只在订单类型是拼团时有用")
    private Integer userGroupBuyingId;

    @ApiModelProperty("用户id")
    private Integer userId;

    // ----------------地址信息----------------

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("地址：省")
    private String addressProvince;

    @ApiModelProperty("地址：市")
    private String addressCity;

    @ApiModelProperty("地址：区")
    private String addressArea;

    @ApiModelProperty("地址：街道")
    private String addressStreet;

    @ApiModelProperty("详细地址")
    private String addressDetail;

    @ApiModelProperty("收货人手机号码")
    private String receiverPhoneNumber;

    @ApiModelProperty("收货人姓名")
    private String receiverName;

    // ----------------额外信息----------------

    @ApiModelProperty("邮费")
    private BigDecimal postage;

    @ApiModelProperty("物流编号")
    private String logisticsNumber;

    @ApiModelProperty("订单发货时间")
    private Date sendTime;

    @ApiModelProperty("确认收货时间")
    private Date receiveTime;

    @ApiModelProperty("支付时间")
    private Date payTime;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalMoney;

    @ApiModelProperty("实际支付金额")
    private BigDecimal realPayMoney;

    // --------------------卡券相关--------------------

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

    @ApiModelProperty("订单条目")
    private List<OrderItemVO> orderItems;

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("商品简介")
    private String commodityIntroduction;

    @Lob
    @ApiModelProperty("商品详情")
    private String commodityDetail;

    @ApiModelProperty("商品价格")
    private BigDecimal commodityPrice;

    @ApiModelProperty("下单时用户的收货地址")
    private String receiveAddress;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
