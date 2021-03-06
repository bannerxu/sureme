package top.banner.service.search.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.banner.models.order.OrderStatusEnum;
import top.banner.models.order.OrderTypeEnum;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class SearchOrderResultVO {
    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("订单状态")
    private OrderStatusEnum orderStatus;

    @ApiModelProperty("订单类型")
    private OrderTypeEnum orderType;

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


    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("折扣价")
    private BigDecimal discountPrice;


    @ApiModelProperty("用户id")
    private Integer userId;

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

    @ApiModelProperty("邮费")
    private BigDecimal postage;

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

    @ApiModelProperty("订单条目")
    private List<SearchOrderItemVO> orderItems;

}
