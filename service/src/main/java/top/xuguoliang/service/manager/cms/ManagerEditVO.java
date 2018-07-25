package top.xuguoliang.service.manager.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotNull;

@Data
public class ManagerEditVO {
    @ApiModelProperty("管理员id")
    @NotNull(message = "管理员id不能为空")
    private Integer managerId;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;
}
