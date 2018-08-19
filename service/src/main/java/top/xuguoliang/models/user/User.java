package top.xuguoliang.models.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("用户")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("性别 0-未知 1-男 2-女")
    private Integer gender;

    @ApiModelProperty("个性签名")
    private String signature;

    @ApiModelProperty("用户所在城市")
    private String city;

    @ApiModelProperty("openId")
    private String openId;

    @Transient
    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

    // 用户信息，针对项目资料 收货地址

    @ApiModelProperty("收货地址")
    private String receiveAddress;

    @ApiModelProperty("积分")
    private String integral;

    @ApiModelProperty("孕期")
    private PregnancyTypeEnum pregnancyType;

    @ApiModelProperty("怀孕日期")
    private Date pregnantDate;

    @ApiModelProperty("宝宝生日")
    private Date babyBirthday;

}
