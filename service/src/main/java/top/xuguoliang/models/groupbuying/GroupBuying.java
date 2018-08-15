package top.xuguoliang.models.groupbuying;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("拼团，团购")
public class GroupBuying {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键id")
    private Integer groupBuyingId;

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

    @ApiModelProperty("拼团价格")
    private BigDecimal groupPrice;

    @ApiModelProperty("拼团人数")
    private Integer peopleNumber;

    @ApiModelProperty("开始时间")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted;

}
