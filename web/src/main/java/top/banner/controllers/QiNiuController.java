package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.banner.service.qiniu.QiNiuService;
import top.banner.service.qiniu.QiNiuMany;
import top.banner.service.qiniu.QiNiuOne;
import top.banner.service.qiniu.QiNiuToken;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "七牛图片上传")
@RequestMapping("/api/qiniu/")
public class QiNiuController {

    @Resource
    private QiNiuService qiniuService;

    /**
     * 返回Token给前端上传
     *
     * @param key
     * @return Token
     */
    @GetMapping("token")
    @ApiOperation("前端上传: 返回Token")
    public QiNiuToken getToken(@RequestParam(required = false) String key) {
        return qiniuService.getToken(key);
    }

    /**
     * 后端上传
     *
     * @param file
     * @return 单文件地址
     */
    @PostMapping("one")
    @ApiOperation("后台上传: 单文件")
    public QiNiuOne uploadOne(@NotNull MultipartFile file) {
        QiNiuOne uploadOneVO = new QiNiuOne();
        uploadOneVO.setFileURL(qiniuService.upload(file));
        return uploadOneVO;
    }

    /**
     * 后端批量上传
     *
     * @param files
     * @return 多文件地址集合
     */
    @PostMapping("many")
    @ApiOperation("后台上传: 多文件")
    public QiNiuMany uploadMany(@NotEmpty MultipartFile[] files) {
        QiNiuMany uploadManyVO = new QiNiuMany();
        List<String> fileURLList = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            String fileURL = qiniuService.upload(file);
            fileURLList.add(fileURL);
        }
        uploadManyVO.setFileURLList(fileURLList);
        return uploadManyVO;
    }

    /**
     * 后端文件上传，流方式
     *
     * @param inputStream
     * @return
     */
    @PostMapping("stream")
    @ApiOperation("后台上传: 流上传")
    public QiNiuOne uploadByStream(InputStream inputStream) {
        QiNiuOne uploadOneVO = new QiNiuOne();
        uploadOneVO.setFileURL(qiniuService.uploadByStream(inputStream));
        return uploadOneVO;
    }

    /**
     * 后端文件上传，字节方式
     *
     * @param byteData
     * @return
     */
    @PostMapping("byte")
    @ApiOperation("后台上传: 字节上传")
    public QiNiuOne uploadByByte(byte[] byteData) {
        QiNiuOne uploadOneVO = new QiNiuOne();
        uploadOneVO.setFileURL(qiniuService.uploadByByte(byteData));
        return uploadOneVO;
    }

}