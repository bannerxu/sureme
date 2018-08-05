package top.xuguoliang.models.shareitem;

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
@ApiModel("分销记录表")
public class ShareItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer shareItemId;

    @ApiModelProperty("上线id")
    private Integer shareUserId;

    @ApiModelProperty("下线id")
    private Integer beShareUserId;

}
