package top.xuguoliang.models.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.xuguoliang.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author jinguoguo
 */
@Data
@Entity
@ApiModel("文章")
public class Article extends BaseEntity {

    @Id
    @ApiModelProperty("文章id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer articleId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("发表文章的用户id")
    private Integer userId;

}
