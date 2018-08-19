package top.xuguoliang.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.xuguoliang.models.withdraw.WithdrawStatus;
import top.xuguoliang.service.withdraw.WithdrawCmsService;
import top.xuguoliang.service.withdraw.cms.WithdrawPageVO;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;

@RestController
@RequestMapping("api/withdraw")
public class WithdrawController {
    @Resource
    private WithdrawCmsService withdrawCmsService;

    @ApiOperation("提现列表")
    @GetMapping("page")
    public Page<WithdrawPageVO> findPage(@ApiParam("用户昵称") @RequestParam(required = false) String nickName,
                                         @ApiParam("状态") @RequestParam(required = false) WithdrawStatus status,
                                         @ApiParam("开始时间") @RequestParam(required = false) Date startTime,
                                         @ApiParam("结束时间") @RequestParam(required = false) Date endTime,
                                         @ApiParam("提现订单号") @RequestParam(required = false) String code,
                                         @PageableDefault(sort = "withdrawId", direction = Sort.Direction.ASC) Pageable pageable) {
        return withdrawCmsService.findPage(nickName, status, startTime, endTime, code, pageable);
    }
}
