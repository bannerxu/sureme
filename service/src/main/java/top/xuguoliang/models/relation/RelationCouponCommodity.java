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
@ApiModel("卡券与商品关系")
public class RelationCouponCommodity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("关系id")
    private Integer relationCouponCommodityId;

    @ApiModelProperty("卡券id")
    private Integer couponId;

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private boolean deleted;
}
