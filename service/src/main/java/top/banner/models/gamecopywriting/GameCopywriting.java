package top.banner.models.gamecopywriting;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @description: 游戏文案
 * @author: XGL
 * @create: 2018-08-29 10:25
 **/
@Data
@Entity
public class GameCopywriting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("文案Id")
    private Integer gameCopywritingId;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("对应的情绪分类")
    private MoodType moodType;

    @ApiModelProperty("是否删除")
    private Boolean deleted = false;





//        Result result = JSONObject.parseObject(s, Result.class);
//
//        List<Faces> faces = result.getFaces();
////        System.out.println(faces);
////        System.out.println(faces.size());
//        Faces faces1 = faces.get(0);
//        Emotion emotion = faces1.getAttributes().getEmotion();
//        System.out.println(emotion);


}
