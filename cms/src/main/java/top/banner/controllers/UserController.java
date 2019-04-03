package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.common.constants.RoleConstant;
import top.banner.models.user.User;
import top.banner.service.user.UserCmsService;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Api(tags = "用户")
@RequestMapping("api/user")
@RestController
public class UserController {

    @Resource
    private UserCmsService userCmsService;

    @ApiOperation("分页列表")
    @GetMapping("page")
    @RequiresRoles(logical = Logical.OR, value = {RoleConstant.ROOT, RoleConstant.ADMIN})
    public Page<User> findPage(@RequestParam(required = false) String nickName,
                               @PageableDefault(sort = "userId", direction = Sort.Direction.DESC) Pageable pageable) {
        return userCmsService.findPage(nickName, pageable);
    }

    @ApiOperation("修改")
    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userCmsService.updateUser(user);
    }

    @ApiOperation("删除")
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        userCmsService.deleteUser(userId);
    }

}
