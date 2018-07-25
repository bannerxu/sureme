package top.xuguoliang.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.xuguoliang.common.constants.RoleConstant;
import top.xuguoliang.models.manager.Manager;
import top.xuguoliang.service.manager.ManagerService;
import top.xuguoliang.service.manager.cms.ManagerAddVO;
import top.xuguoliang.service.manager.cms.ManagerEditVO;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * @author jinguoguo
 */
@RestController
@Api(tags = "管理员模块")
@RequestMapping("api/manager")
public class ManagerController {

    @Resource
    private Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @Resource
    private ManagerService managerService;

    @GetMapping("page")
    @ApiOperation("分页列表")
    @RequiresRoles(logical = Logical.OR, value = RoleConstant.ROOT)
    public Page<Manager> findPage(@PageableDefault(sort = "managerId", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.debug("调用接口：分页查询管理员");
        return managerService.findPage(pageable);
    }

    @PostMapping
    @ApiOperation("添加")
    @RequiresRoles(logical = Logical.OR, value = RoleConstant.ROOT)
    public Manager add(@RequestBody @Valid ManagerAddVO addVO) {
        logger.debug("调用接口：添加管理员");
        return managerService.add(addVO);
    }

    @PutMapping
    @ApiOperation("更新")
    @ApiIgnore
    @RequiresRoles(logical = Logical.OR, value = RoleConstant.ROOT)
    public Manager update(@RequestBody @Valid ManagerEditVO editVO) {
        logger.debug("调用接口：更新管理员");
        return managerService.update(editVO);
    }

    @DeleteMapping
    @ApiOperation("删除")
    @RequiresRoles(logical = Logical.OR, value = RoleConstant.ROOT)
    public void delete(@RequestParam int managerId) {
        logger.debug("调用接口：删除管理员");
        managerService.delete(managerId);
    }

}
