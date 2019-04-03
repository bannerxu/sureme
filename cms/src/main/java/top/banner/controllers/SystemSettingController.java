package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.banner.common.constants.RoleConstant;
import top.banner.models.systemsetting.SystemSetting;
import top.banner.service.systemsetting.SystemSettingService;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/systemSetting")
@Api(tags = "系统设置")
public class SystemSettingController {

    @Resource
    private SystemSettingService systemSettingService;

    @GetMapping
    @ApiOperation("获取设置")
    @RequiresRoles(value = {RoleConstant.ROOT})
    public SystemSetting getSystemSetting() {
        return systemSettingService.getSystemSetting();
    }

    @PutMapping
    @ApiOperation("修改设置")
    @RequiresRoles(value = {RoleConstant.ROOT})
    public SystemSetting modifySystemSetting(SystemSetting systemSetting) {
        return systemSettingService.modifySystemSetting(systemSetting);
    }
}
