package top.xuguoliang.models.article;

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
@ApiModel("文章Banner轮播")
public class ArticleBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("banner id")
    private Integer articleBannerId;

    @ApiModelProperty("banner url")
    private String articleBannerUrl;

    @ApiModelProperty("对应文章id")
    private Integer articleId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除")
    private Boolean deleted = false;

}
