package top.banner.service.user.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class AuthorizeVO {

    @ApiModelProperty("密文")
    @NotBlank(message = "密文不能为空")
    private String data;

    @ApiModelProperty("iv")
    @NotBlank(message = "iv不能为空")
    private String iv;

    @ApiModelProperty("code")
    @NotBlank(message = "code不能为空")
    private String code;

}