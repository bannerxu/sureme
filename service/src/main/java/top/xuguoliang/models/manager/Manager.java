package top.xuguoliang.models.manager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("管理员id")
    private Integer managerId;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    @JsonIgnore
    private String password;

    @ApiModelProperty("角色。0：普通管理员，1：超级管理员")
    @NotNull
    private Integer role = 0;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

    @Transient
    @ApiModelProperty("token")
    private String token;


}
