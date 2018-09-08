package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.service.apply.ApplyRecordService;
import top.xuguoliang.service.apply.cms.ApplyRecordVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/audit")
@Api(tags = "审核")
public class AuditController {

    @Resource
    private ApplyRecordService applyRecordService;

    @GetMapping
    @ApiOperation("分页查询")
    public Page<ApplyRecordVO> findPageApplyRecord(@PageableDefault(sort = "applyTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return applyRecordService.findPageApplyRecord(pageable);
    }

    @PutMapping("/{applyRecordId}")
    @ApiOperation("审核")
    public void audit(@PathVariable Integer applyRecordId, @RequestBody @ApiParam("是否通过 0-否 1-是") Integer isPass) {
        applyRecordService.audit(applyRecordId, isPass);
    }


}
