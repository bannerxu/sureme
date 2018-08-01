package top.xuguoliang.service.coupon.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class CouponCmsAddVO {

    @NotBlank(message = "卡券名不能为空")
    @ApiModelProperty("卡券名")
    private String couponName;

    @NotNull(message = "商品不能为空")
    @ApiModelProperty("商品id，为零全适用，非零针对单一商品")
    private List<Integer> commodityIds;

    @NotNull(message = "折扣不能为空")
    @ApiModelProperty("对应折扣")
    private BigDecimal couponDiscount;

}