package top.xuguoliang.models.commodity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author jinguoguo
 */
@ApiModel("商品")
@Entity
@Data
public class Commodity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("销量")
    private Integer salesVolume;

    @Lob
    @ApiModelProperty("商品详情")
    private String commodityDetail;

    @ApiModelProperty("商品价格")
    private BigDecimal commodityPrice;

}
