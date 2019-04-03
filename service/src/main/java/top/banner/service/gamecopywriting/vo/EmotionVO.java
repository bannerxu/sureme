package top.banner.service.gamecopywriting.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.banner.models.gamecopywriting.MoodType;

/**
 * @description:
 * @author: XGL
 * @create: 2018-08-29 19:17
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionVO {
    private MoodType key;
    private Double value;
}
