package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xuguoliang.service.second.SecondWebService;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/second")
@Api(tags = "秒杀模块")
public class SecondController {

    @Resource
    private SecondWebService secondWebService;
}
