package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.second.SecondWebService;
import top.xuguoliang.service.second.web.SecondKillResultVO;
import top.xuguoliang.service.second.web.SecondWebDetailVO;
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

    @GetMapping("/{secondId}")
    @ApiOperation("详情")
    public SecondWebDetailVO getDetail(@PathVariable Integer secondId) {
        return secondWebService.getDetail(secondId);
    }

    @PostMapping
    @ApiOperation("秒杀")
    public SecondKillResultVO secondKill(@PathVariable Integer secondId) {
        return null;
    }
}
