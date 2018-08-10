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
@ApiModel("喜欢，点赞")
public class ArticleLike {

    @ApiModelProperty("主键id")
    private Integer articleLikeId;

    @ApiModelProperty("文章id")
    private Integer articleId;

    @ApiModelProperty("用户id")
    private Integer userId;

}
