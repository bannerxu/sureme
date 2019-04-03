package top.banner.service.image;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImgUrlVO {
    @ApiModelProperty("地址")
    private String url;
}
