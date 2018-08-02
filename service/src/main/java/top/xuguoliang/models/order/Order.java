package top.xuguoliang.models.order;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单需要冗余多张表的字段
 * @author jinguoguo
 */
@ApiModel("订单")
@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单状态")
    private Integer orderStatus;

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

    // 用户信息

    @ApiModelProperty("下单时用户的收货地址")
    private String receiveAddress;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

}
