package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.banner.common.utils.WeChatUtil;

import javax.annotation.Resource;

@RestController
@Api(tags = "微信相关")
@RequestMapping("/api/weChat")
public class WeChatController {
    @Resource
    private WeChatUtil weChatUtil;


    @ApiIgnore
    @ApiOperation("获取小程序AccessToken")
    @GetMapping("getAccessToken")
    public String getAccessToken() {
        return weChatUtil.getAccessToken();
    }

    @ApiOperation("小程序二维码")
    @GetMapping("getQRCode")
    public String getQRCode(@ApiParam("大小") @RequestParam Integer width,
                            @ApiParam("跳转地址") @RequestParam String path,
                            @ApiParam("业务参数，不可以包含中文") @RequestParam String scene) {
        return weChatUtil.getQRCode(width, path, scene);
    }

}
