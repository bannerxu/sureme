package top.xuguoliang.models.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("文章收藏")
public class ArticleStar {

    @ApiModelProperty("主键id")
    private Integer articleStarId;

    @ApiModelProperty("文章id")
    private Integer articleId;

    @ApiModelProperty("用户id")
    private Integer userId;

}
