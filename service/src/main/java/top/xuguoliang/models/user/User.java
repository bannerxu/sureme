package top.xuguoliang.models.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import top.xuguoliang.common.BaseEntity;

import javax.persistence.*;

/**
 * @author jinguoguo
 */
@Entity
@ApiModel("用户")
public class User extends BaseEntity {

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

    /**
        用户信息，针对项目资料
        收货地址
     */
    @ApiModelProperty("收货地址")
    private String receiveAddress;

    @ApiModelProperty("积分")
    private String integral;
}
