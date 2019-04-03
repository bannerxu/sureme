package top.banner.service.user.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author jinguoguo
 */
@Data
public class AddressWebUpdateParamVO {

    @NotNull
    @ApiModelProperty("地址id")
    private Integer addressId;

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private Integer userId;

    @NotBlank(message = "地址不能为空")
    @ApiModelProperty("地址")
    private String address;

    @NotBlank(message = "省份不能为空")
    @ApiModelProperty("地址：省")
    private String addressProvince;

    @NotBlank(message = "城市不能为空")
    @ApiModelProperty("地址：市")
    private String addressCity;

    @NotBlank(message = "区域不能为空")
    @ApiModelProperty("地址：区")
    private String addressArea;

    @NotBlank(message = "街道不能为空")
    @ApiModelProperty("地址：街道")
    private String addressStreet;

    @NotBlank(message = "详细地址不能为空")
    @ApiModelProperty("详细地址")
    private String addressDetail;

    @NotBlank(message = "收货人手机号码不能为空")
    @ApiModelProperty("收货人手机号码")
    private String receiverPhoneNumber;

    @NotBlank(message = "收货人姓名不能为空")
    @ApiModelProperty("收货人姓名")
    private String receiverName;
}
