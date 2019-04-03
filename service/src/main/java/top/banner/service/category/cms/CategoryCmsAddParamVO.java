package top.banner.service.category.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinguoguo
 */
@Data
public class CategoryCmsAddParamVO {

    @ApiModelProperty("分类名")
    private String categoryName;

}
