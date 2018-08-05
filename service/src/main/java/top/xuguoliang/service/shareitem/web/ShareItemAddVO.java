package top.xuguoliang.service.shareitem.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author jinguoguo
 */
@Data
@ApiModel("分享下线记录添加")
public class ShareItemAddVO {
    @NotNull(message = "上线id不能为空")
    @ApiModelProperty("上线id")
    private Integer shareUserId;

    @NotNull(message = "下线id不能为空")
    @ApiModelProperty("下线id")
    private Integer beShareUserId;
}
