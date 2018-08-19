package top.xuguoliang.models.logistics;

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
@ApiModel("物流记录")
public class LogisticsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("物流记录")
    private Integer logisticsRecordId;

    @ApiModelProperty("1表示此快递单的物流信息不会发生变化，此时您可缓存下来；0表示有变化的可能性")
    private Integer status;

    @ApiModelProperty("快递单号")
    private String no;

    @ApiModelProperty("快递公司名字")
    private String company;

    @ApiModelProperty("快递公司代码")
    private String com;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("物流事件发生的时间")
    private Date datetime;

    @ApiModelProperty("物流事件的描述")
    private String remark;

}
