package top.xuguoliang.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.xuguoliang.models.user.Address;
import top.xuguoliang.service.user.AddressWebService;
import top.xuguoliang.service.user.web.AddressWebAddParamVO;
import top.xuguoliang.service.user.web.AddressWebUpdateParamVO;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author jinguoguo
 */
@RestController
@RequestMapping("api/user/address")
@Api(tags = "用户地址模块")
public class AddressController {

    @Resource
    private AddressWebService addressWebService;

    @PostMapping
    @ApiOperation("添加地址")
    public Address addAddress(@RequestBody AddressWebAddParamVO addressWebAddParamVO) {
        return addressWebService.addAddress(addressWebAddParamVO);
    }

    @GetMapping("page")
    @ApiOperation("分页查询")
    public Page<Address> findPage(@PageableDefault Pageable pageable) {
        return addressWebService.findPage(pageable);
    }

    @GetMapping("/{addressId}")
    @ApiOperation("单个查询")
    public Address getAddress(@PathVariable @NotNull(message = "id不能为空") Integer addressId) {
        return addressWebService.getAddress(addressId);
    }

    @PutMapping("/{addressId}")
    @ApiOperation("修改")
    public Address updateAddress(@PathVariable @NotNull(message = "id不能为空") Integer addressId,
                                 @RequestBody AddressWebUpdateParamVO addressWebUpdateParamVO) {
        return addressWebService.updateAddress(addressId, addressWebUpdateParamVO);
    }

    @DeleteMapping("/{addressId}")
    @ApiOperation("删除")
    public void deleteAddress(@PathVariable @NotNull(message = "id不能为空") Integer addressId) {
        addressWebService.deleteAddress(addressId);
    }

    @PutMapping("default/{addressId}")
    @ApiOperation("设置默认地址")
    public void setDefaultAddress(@PathVariable @NotNull(message = "id不能为空") Integer addressId) {
        Integer userId = UserHelper.getUserId();
        addressWebService.setDefaultAddress(userId, addressId);
    }

}
