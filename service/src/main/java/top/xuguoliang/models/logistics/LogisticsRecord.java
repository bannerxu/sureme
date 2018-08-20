package top.xuguoliang.models.logistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("物流记录")
public class LogisticsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("物流记录")
    private Integer logisticsRecordId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("物流单号")
    private String logisticsNumber;

    @ApiModelProperty("物流公司")
    private String logisticsCompany;

    @Lob
    @ApiModelProperty("物流信息")
    private String logisticsInfo;

}
