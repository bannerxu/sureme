package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.service.groupbuying.GroupBuyingWebService;
import top.banner.service.groupbuying.web.*;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/groupBuying")
@Api(tags = "拼团模块")
public class GroupBuyingController {

    @Resource
    private GroupBuyingWebService groupBuyingWebService;

    @GetMapping("findPageGroupBuying")
    @ApiOperation("拼团列表")
    public Page<GroupBuyingWebResultVO> findPageGroupBuying(
            @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return groupBuyingWebService.findPageGroupBuying(pageable);
    }

    @GetMapping("findPageUserGroupBuying")
    @ApiOperation("用户拼团列表")
    public Page<UserGroupBuyingWebResultVO> findPageUserGroupBuying(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return groupBuyingWebService.findPageUserGroupBuying(pageable);
    }

    @GetMapping("/{groupBuyingId}")
    @ApiOperation("拼团详情")
    public GroupBuyingWebDetailVO getGroupBuying(@PathVariable Integer groupBuyingId){
        return groupBuyingWebService.getGroupBuying(groupBuyingId);
    }

    @GetMapping("userGroupBuying/{userGroupBuyingId}")
    @ApiOperation("用户拼团详情")
    public UserGroupBuyingWebDetailVO getUserGroupBuying(@PathVariable Integer userGroupBuyingId) {
        return groupBuyingWebService.getUserGroupBuying(userGroupBuyingId);
    }

    @PostMapping("open")
    @ApiOperation("开团")
    public UserGroupBuyingWebOpenResultVO openUserGroupBuying(@RequestBody UserGroupBuyingWebOpenParamVO vo) {
        Integer userId = UserHelper.getUserId();
        return groupBuyingWebService.openUserGroupBuying(userId, vo);
    }

    @PostMapping("join")
    @ApiOperation("加入拼团")
    public UserGroupBuyingWebResultVO joinUserGroupBuying(@RequestBody UserGroupBuyingWebJoinParamVO vo) {
        Integer userId = UserHelper.getUserId();
        return groupBuyingWebService.joinUserGroupBuying(userId, vo);
    }
}
