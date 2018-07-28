package top.xuguoliang.models.commodity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import top.xuguoliang.common.BaseEntity;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * @author jinguoguo
 */
@Entity
@ApiModel("最小库存单元，规格，SKU")
public class StockKeepingUnit extends BaseEntity {

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("商品id")
    private Integer commodityId;

}
