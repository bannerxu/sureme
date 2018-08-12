package top.xuguoliang.service.coupon.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
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

    @ApiModelProperty("领取开始时间")
    private Date pullBeginTime;

    @ApiModelProperty("领取结束时间")
    private Date pullEndTime;

    @ApiModelProperty("使用开始时间")
    private Date useBeginTime;

    @ApiModelProperty("使用结束时间")
    private Date useEndTime;


}
