package top.banner.service.user.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.banner.models.user.PregnancyTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
public class UserSetPregnancyVO {
    @NotNull(message = "孕期不能为空")
    @ApiModelProperty("孕期")
    private PregnancyTypeEnum pregnancyType;

    @ApiModelProperty("怀孕日期")
    private Date pregnantDate;

    @ApiModelProperty("宝宝生日")
    private Date babyBirthday;
}
