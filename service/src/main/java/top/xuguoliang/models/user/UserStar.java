package top.xuguoliang.models.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("用户收藏")
public class UserStar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("userStarId")
    private Integer userStarId;

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("删除")
    private boolean deleted;

}
