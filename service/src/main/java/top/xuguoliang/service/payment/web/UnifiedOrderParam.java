package top.xuguoliang.service.payment.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UnifiedOrderParam {
    @ApiModelProperty("小程序id")
    private String appid;

    @ApiModelProperty("商户号id")
    private String mch_id;

    @ApiModelProperty("随机字符串")
    private String nonce_str;

    @ApiModelProperty("签名类型")
    private String sign_type;

    @ApiModelProperty("签名")
    private String sign;

    @ApiModelProperty("商品描述")
    private String body;

    @ApiModelProperty("商户订单号")
    private String out_trade_no;

    @ApiModelProperty("标价金额，单位为分")
    private int total_fee;

    @ApiModelProperty("终端ip")
    private String spbill_create_ip;

    @ApiModelProperty("通知地址")
    private String notify_url;

    @ApiModelProperty("交易类型")
    private String trade_type;

    @ApiModelProperty("用户标识")
    private String openid;

}
