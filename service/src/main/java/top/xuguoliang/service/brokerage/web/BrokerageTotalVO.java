package top.xuguoliang.service.brokerage.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("累计佣金总额")
public class BrokerageTotalVO {
    @ApiModelProperty("累计佣金总额")
    private BigDecimal totalMoney;
}
