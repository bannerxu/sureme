package top.banner.service.payment.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class UnifiedOrderResult {

    @ApiModelProperty("返回码")
    private String return_code;

    @ApiModelProperty("返回消息")
    private String return_msg;

    @ApiModelProperty("结果码")
    private String result_code;

    @ApiModelProperty("小程序id")
    private String appid;

    @ApiModelProperty("商户号id")
    private String mch_id;

    @ApiModelProperty("随机字符串")
    private String nonce_str;

    @ApiModelProperty("签名")
    private String sign;

    @ApiModelProperty("预支付id")
    private String prepay_id;

    @ApiModelProperty("类型")
    private String trade_type;

}
