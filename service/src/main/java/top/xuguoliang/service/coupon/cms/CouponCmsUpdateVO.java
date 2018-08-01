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
public class CouponCmsUpdateVO {

    @NotNull(message = "id不能为空")
    @ApiModelProperty("卡券id")
    private Integer couponId;

    @NotBlank(message = "卡券名不能为空")
    @ApiModelProperty("卡券名")
    private String couponName;

    @NotNull(message = "折扣不能为空")
    @ApiModelProperty("对应折扣")
    private BigDecimal couponDiscount;

    @ApiModelProperty("数组：商品id")
    private List<Integer> commodityIds;

}
