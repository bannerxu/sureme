package top.xuguoliang.service.groupbuying.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.models.commodity.CommodityBanner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Data
public class UserGroupBuyingWebDetailVO {

    @ApiModelProperty("用户拼团id")
    private Integer userGroupBuyingId;

    @ApiModelProperty("原价")
    private String originalPrice;

    @ApiModelProperty("最大人数")
    private Integer maxPeopleNumber;

    @ApiModelProperty("当前人数")
    private Integer currentPeopleNumber;

    @ApiModelProperty("是否满人")
    private Boolean isFull;

    // ---------------------拼团信息---------------------

    @ApiModelProperty("拼团价格")
    private BigDecimal groupPrice;

    @ApiModelProperty("拼团人数")
    private Integer peopleNumber;

    @ApiModelProperty("开始时间")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted;

    // ---------------------商品信息---------------------

    @ApiModelProperty("商品id")
    private Integer commodityId;

    @ApiModelProperty("商品标题")
    private String commodityTitle;

    @ApiModelProperty("销量")
    private Integer salesVolume;

    @ApiModelProperty("商品简介")
    private String commodityIntroduction;

    @ApiModelProperty("商品详情")
    private String commodityDetail;

    @ApiModelProperty("商品价格")
    private BigDecimal commodityPrice;

    @ApiModelProperty("分类id")
    private Integer categoryId;

    @ApiModelProperty("分类名")
    private String categoryName;

    @ApiModelProperty("商品轮播")
    private List<CommodityBanner> commodityBanners;

    // ---------------------规格信息---------------------

    @ApiModelProperty("规格id")
    private Integer stockKeepingUnitId;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("折扣价")
    private BigDecimal discountPrice;

    @ApiModelProperty("库存")
    private Integer stock;

    @ApiModelProperty("发起者的用户id")
    private Integer sponsorUserId;

    @ApiModelProperty("对应拼团id")
    private Integer groupBuyingId;


}