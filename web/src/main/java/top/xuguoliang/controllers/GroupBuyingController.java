package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.groupbuying.GroupBuyingWebService;
import top.xuguoliang.service.groupbuying.web.GroupBuyingWebJoinParamVO;
import top.xuguoliang.service.groupbuying.web.GroupBuyingWebResultVO;

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

    @GetMapping
    @ApiOperation("拼团列表")
    public Page<GroupBuyingWebResultVO> findPage(@PageableDefault Pageable pageable) {
        return groupBuyingWebService.findPage(pageable);
    }

    @GetMapping("/{groupBuyingId}")
    @ApiOperation("拼团详情")
    public GroupBuyingWebResultVO getGroupBuying(@PathVariable Integer groupBuyingId){
        return groupBuyingWebService.getGroupBuying(groupBuyingId);
    }

    @PostMapping("join")
    @ApiOperation("加入拼团")
    public GroupBuyingWebResultVO joinGroupBuying(@RequestBody GroupBuyingWebJoinParamVO groupBuyingWebJoinParamVO) {
        Integer userId = UserHelper.getUserId();
        return groupBuyingWebService.joinGroupBuying(userId, groupBuyingWebJoinParamVO);
    }
}