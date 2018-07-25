package top.xuguoliang.service.Image;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImgUrlVO {
    @ApiModelProperty("地址")
    private String url;
}
