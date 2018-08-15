package top.xuguoliang.service.groupbuying.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class GroupBuyingCmsAddParamVO {

    @NotNull(message = "商品不能为空")
    @ApiModelProperty("商品id")
    private Integer commodityId;

    @NotNull(message = "规格不能为空")
    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

    @NotNull(message = "拼团价格不能为空")
    @ApiModelProperty("拼团价格")
    private BigDecimal groupPrice;

    @NotNull(message = "拼团人数不能为空")
    @ApiModelProperty("拼团人数")
    private Integer peopleNumber;

    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty("开始时间")
    private Date beginTime;

    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty("结束时间")
    private Date endTime;

}
