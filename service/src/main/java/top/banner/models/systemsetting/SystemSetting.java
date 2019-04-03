package top.banner.models.systemsetting;

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
@ApiModel("系统设置")
public class SystemSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("系统设置")
    private Integer systemSettingId;

    @ApiModelProperty("第一级佣金比例")
    private Double firstScale;

    @ApiModelProperty("第二级佣金比例")
    private Double secondScale;

    @ApiModelProperty("第三级佣金比例")
    private Double thirdScale;

}
