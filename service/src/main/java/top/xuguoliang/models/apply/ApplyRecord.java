package top.xuguoliang.models.apply;

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
@ApiModel("申请记录")
public class ApplyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("申请记录id")
    private Integer applyRecordId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("原因")
    private String applyReason;

    @ApiModelProperty("申请类型")
    private ApplyType applyType;

    @ApiModelProperty("申请状态")
    private ApplyStatus applyStatus;

    @ApiModelProperty("申请时间")
    private Date applyTime;

    @ApiModelProperty("审核时间")
    private Date auditTime;

}
