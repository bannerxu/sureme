package top.banner.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.banner.service.moneywater.MoneyWaterService;
import top.banner.service.moneywater.cms.MoneyWaterVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/moneyWater")
@Api(tags = "资金流水")
public class MoneyWaterController {

    @Resource
    private MoneyWaterService moneyWaterService;

    @GetMapping
    @ApiOperation("分页查询所有")
    public Page<MoneyWaterVO> findAll(@PageableDefault(sort = "time", direction = Sort.Direction.DESC) Pageable pageable) {
        return moneyWaterService.findAll(pageable);
    }

}
