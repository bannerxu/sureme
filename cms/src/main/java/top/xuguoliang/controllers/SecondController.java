package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.common.constants.RoleConstant;
import top.xuguoliang.service.second.SecondCmsService;
import top.xuguoliang.service.second.cms.SecondCmsAddParamVO;
import top.xuguoliang.service.second.cms.SecondCmsAddResultVO;
import top.xuguoliang.service.second.cms.SecondCmsDetailVO;
import top.xuguoliang.service.second.cms.SecondCmsPageResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/second")
@Api(tags = "秒杀模块")
public class SecondController {

    @Resource
    private SecondCmsService secondCmsService;


    @GetMapping
    @ApiOperation("分页")
    @RequiresRoles({RoleConstant.ADMIN, RoleConstant.ROOT})
    public Page<SecondCmsPageResultVO> findPage(@PageableDefault Pageable pageable) {
        return secondCmsService.findPage(pageable);
    }


    @GetMapping("/{secondId}")
    @ApiOperation("详情")
    @RequiresRoles({RoleConstant.ADMIN, RoleConstant.ROOT})
    public SecondCmsDetailVO getSecondDetail(@PathVariable Integer secondId) {
        return secondCmsService.getSecondDetail(secondId);
    }


    @PostMapping
    @ApiOperation("添加")
    @RequiresRoles({RoleConstant.ADMIN, RoleConstant.ROOT})
    public SecondCmsAddResultVO addSecond(@RequestBody SecondCmsAddParamVO paramVO) {
        return secondCmsService.addSecond(paramVO);
    }


    @DeleteMapping("/{secondId}")
    @ApiOperation("删除")
    @RequiresRoles({RoleConstant.ADMIN, RoleConstant.ROOT})
    public void deleted(@PathVariable Integer secondId) {
        secondCmsService.deleteSecond(secondId);
    }

}
