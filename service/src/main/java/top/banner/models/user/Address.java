package top.banner.models.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("用户收货地址")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String addressArea;

    @ApiModelProperty("地址：街道")
    private String addressStreet;

    @ApiModelProperty("详细地址")
    private String addressDetail;

    @ApiModelProperty("收货人手机号码")
    private String receiverPhoneNumber;

    @ApiModelProperty("收货人姓名")
    private String receiverName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

    @ApiModelProperty("默认")
    private Boolean isDefault = false;

}
