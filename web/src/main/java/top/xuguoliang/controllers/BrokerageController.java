package top.xuguoliang.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.models.brokerage.Brokerage;
import top.xuguoliang.models.shareitem.ShareItem;
import top.xuguoliang.models.user.User;
import top.xuguoliang.service.brokerage.BrokerageWebService;
import top.xuguoliang.service.brokerage.web.BrokerageTotalVO;
import top.xuguoliang.service.brokerage.web.BrokerageVO;
import top.xuguoliang.service.shareitem.ShareItemWebService;
import top.xuguoliang.service.shareitem.web.ShareItemAddVO;
import top.xuguoliang.service.shareitem.web.ShareItemVO;

import javax.annotation.Resource;
import java.math.BigDecimal;


/**
 * @author jinguoguo
 */
@RestController
@Api(tags = "佣金")
@RequestMapping("/api/brokerage")
public class BrokerageController {

    @Resource
    private BrokerageWebService brokerageWebService;

    // TODO: 2018-08-05 缺少累计收益接口

    @ApiOperation("累计佣金数")
    @GetMapping("total")
    public BrokerageTotalVO getTotal(){
        return brokerageWebService.getTotal(UserHelper.getUserId());
    }


    @ApiOperation("佣金明细")
    @GetMapping("page")
    public Page<BrokerageVO> findPage(@PageableDefault(sort = "brokerageId", direction = Sort.Direction.ASC) Pageable pageable) {
        return brokerageWebService.findPage(UserHelper.getUserId(), pageable);
    }

    @ApiOperation("佣金提现")
    @PutMapping("withdraw/{money}")
    public User withdraw(@PathVariable BigDecimal money) {
        return brokerageWebService.withdraw(money, UserHelper.getUserId());
    }


}


