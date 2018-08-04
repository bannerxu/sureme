package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.xuguoliang.service.image.ImageService;
import top.xuguoliang.service.image.ImgUrlVO;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author jinguoguo
 */
@RestController
@Api(tags = "上传")
@RequestMapping("api/image")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Resource
    private ImageService imageService;

    @PostMapping("upload")
    @ApiOperation("后台上传: 单文件")
    public ImgUrlVO uploadOne(@NotNull MultipartFile file) {
        logger.debug("调用接口：上传单文件");
        return imageService.upload(file);
    }
}
