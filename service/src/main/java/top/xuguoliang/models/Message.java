package top.xuguoliang.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by XGL on 2017-09-15.
 * 房间消息
 */
@Data
public class Message<T> {

    @ApiModelProperty("消息")
    private T message;
    @ApiModelProperty("类型 1-普通消息 2-房间信息 2-发送礼物 3-表情 4-系统消息")
    private Integer type;

    @ApiModelProperty("礼物id")
    private Integer giftId;

    @ApiModelProperty("礼物数量")
    private Integer giftNumber;

    @ApiModelProperty("礼物名")
    private String giftName;
    @ApiModelProperty("总价")
    private BigDecimal totalPrice;

    @ApiModelProperty("发送消息的id")
    private Integer userId;
    @ApiModelProperty("用户名字")
    private String name;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("余额")
    private BigDecimal balance;
    @ApiModelProperty("总人数")
    private Integer totalNumber;



}
