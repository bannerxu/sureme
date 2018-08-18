package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xuguoliang.service.second.SecondWebService;
import top.xuguoliang.service.second.web.SecondWebPageResultVO;

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

    @GetMapping
    @ApiOperation("分页")
    public Page<SecondWebPageResultVO> findPage(@PageableDefault Pageable pageable) {
        return secondWebService.findPage(pageable);
    }
}
