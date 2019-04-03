package top.banner.service.manager.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ManagerAddVO {
    @ApiModelProperty("名字")
    @NotEmpty(message = "管理员名不能为空")
    private String name;

    @ApiModelProperty("账号")
    @NotEmpty(message = "账号不能为空")
    private String account;

    @ApiModelProperty("密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

}
