package top.xuguoliang.models.order;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
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

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("商品详情")
    private String commodityDetail;

    @ApiModelProperty("收货地址")
    private String receiveAddress;

}
