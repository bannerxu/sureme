package top.banner.service.apply.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author jinguoguo
 */
@Data
public class AuditParam {
    @NotNull(message = "是否通过不能为空的")
    @ApiModelProperty("是否通过 0-否 1是")
    private Integer isPass;
}
