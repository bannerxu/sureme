package top.xuguoliang.service.search.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class SearchManagerResultVO {

    @ApiModelProperty("管理员id")
    private Integer managerId;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("账号")
    private String account;

}
