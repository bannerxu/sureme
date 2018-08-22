package top.xuguoliang.service.order.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class ApplyRefundVO {

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("退款类型 1-仅退款 2-退货退款")
    private Integer refundType;

    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("图片")
    private List<String> images;

}
