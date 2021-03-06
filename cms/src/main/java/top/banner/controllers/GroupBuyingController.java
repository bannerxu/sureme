package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.banner.service.groupbuying.GroupBuyingCmsService;
import top.banner.service.groupbuying.cms.*;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/groupBuying")
@Api(tags = "拼团模块")
public class GroupBuyingController {

    @Resource
    private GroupBuyingCmsService groupBuyingCmsService;

    @GetMapping
    @ApiOperation("分页")
    public Page<GroupBuyingCmsResultVO> findPage(
            @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return groupBuyingCmsService.findPage(pageable);
    }

    @GetMapping("/{groupBuyingId}")
    @ApiOperation("查询单个")
    public GroupBuyingCmsDetailVO getGroupBuying(@PathVariable Integer groupBuyingId) {
        return groupBuyingCmsService.getGroupBuying(groupBuyingId);
    }

    @PostMapping
    @ApiOperation("添加")
    public void addGroupBuying(@RequestBody GroupBuyingCmsAddParamVO groupBuyingCmsAddParamVO) {
        groupBuyingCmsService.addGroupBuying(groupBuyingCmsAddParamVO);
    }

    @PutMapping("/{groupBuyingId}")
    @ApiOperation("修改")
    public GroupBuyingCmsResultVO updateGroupBuying(@PathVariable Integer groupBuyingId,
                                                    @RequestBody GroupBuyingCmsUpdateParamVO groupBuyingCmsUpdateParamVO) {
        return groupBuyingCmsService.updateGroupBuying(groupBuyingId, groupBuyingCmsUpdateParamVO);
    }

    @DeleteMapping("/{groupBuyingId}")
    @ApiOperation("删除")
    public void deleteGroupBuying(@PathVariable Integer groupBuyingId) {
        groupBuyingCmsService.deleteGroupBuying(groupBuyingId);
    }

    @GetMapping("userGroupBuying}")
    public Page<UserGroupBuyingCmsResultVO> findPageUserGroupBuying(
            @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return groupBuyingCmsService.findPageUserGroupBuying(pageable);
    }

}
