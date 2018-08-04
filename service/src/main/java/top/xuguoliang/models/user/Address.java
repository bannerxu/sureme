package top.xuguoliang.models.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("用户收货地址")
public class Address {

    @ApiModelProperty("id")
    private Integer addressId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("地址：省")
    private String addressProvince;

    @ApiModelProperty("地址：市")
    private String addressCity;

    @ApiModelProperty("地址：区")
    private String area;

    @ApiModelProperty("地址：街道")
    private String street;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted;
}
