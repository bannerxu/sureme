package top.xuguoliang.models.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import top.xuguoliang.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author jinguoguo
 */
@Entity
@ApiModel("文章Banner轮播")
public class ArticleBanner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("banner id")
    private Integer articleBannerId;

    @ApiModelProperty("banner url")
    private String articleBannerUrl;

    @ApiModelProperty("对应文章id")
    private Integer articleId;

}
