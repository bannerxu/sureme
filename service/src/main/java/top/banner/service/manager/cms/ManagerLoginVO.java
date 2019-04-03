package top.banner.service.manager.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class ManagerLoginVO {
    @ApiModelProperty("账户")
    @NotBlank(message = "账户不能为空")
    private String account;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

}
