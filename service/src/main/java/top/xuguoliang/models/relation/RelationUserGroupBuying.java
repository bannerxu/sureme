package top.xuguoliang.models.relation;

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
@ApiModel("用户、拼团关系")
public class RelationUserGroupBuying {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键id")
    private Integer relationUserGroupBuyingId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("拼团id")
    private Integer groupBuyingId;

    @ApiModelProperty("已支付")
    private Boolean paid;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted;

}
