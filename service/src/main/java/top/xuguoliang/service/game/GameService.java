package top.xuguoliang.service.game;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import springfox.documentation.service.ApiKey;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.HttpUtils;
import top.xuguoliang.models.gamecopywriting.GameCopywriting;
import top.xuguoliang.models.gamecopywriting.GameCopywritingDao;
import top.xuguoliang.models.gamecopywriting.MoodType;
import top.xuguoliang.service.gamecopywriting.vo.Emotion;
import top.xuguoliang.service.gamecopywriting.vo.EmotionVO;
import top.xuguoliang.service.gamecopywriting.vo.Faces;
import top.xuguoliang.service.gamecopywriting.vo.Result;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 游戏
 * @author: XGL
 * @create: 2018-08-29 16:36
 **/
@Service
public class GameService {
    @Value("${game.apiKey}")
    private String apiKey;

    @Value("${game.apiSecret}")
    private String apiSecret;

    @Resource
    private GameCopywritingDao gameCopywritingDao;
    private static final String URL = "https://api-cn.faceplusplus.com/facepp/v3/detect";

    public GameCopywriting game(String imageUrl) throws IOException {
        Result result;
        List<EmotionVO> list = new ArrayList<>(10);

        Map<String, Object> map = new HashMap<>(5);
        map.put("api_key", apiKey);
        map.put("api_secret", apiSecret);
        map.put("image_url", imageUrl);
        map.put("return_attributes", "emotion");
        String str = HttpUtils.httpPostRequest(URL, map);
        if (str.contains("error_message")) {
            throw new ValidationException("测试失败");
        } else {
            result = JSONObject.parseObject(str, Result.class);
        }
        List<Faces> faces = result.getFaces();
        if (faces.size() == 0) {
            throw new ValidationException("测试失败");
        } else {
            Faces f = faces.get(0);
            Emotion emotion = f.getAttributes().getEmotion();

            list.add(new EmotionVO(MoodType.anger, emotion.getAnger()));
            list.add(new EmotionVO(MoodType.disgust, emotion.getDisgust()));
            list.add(new EmotionVO(MoodType.fear, emotion.getFear()));
            list.add(new EmotionVO(MoodType.happiness, emotion.getHappiness()));
            list.add(new EmotionVO(MoodType.neutral, emotion.getNeutral()));
            list.add(new EmotionVO(MoodType.sadness, emotion.getSadness()));
            list.add(new EmotionVO(MoodType.surprise, emotion.getSurprise()));

            List<EmotionVO> collect = list.stream().sorted((e1, e2) -> (e1.getValue().compareTo(e2.getValue()))).collect(Collectors.toList());
            Collections.reverse(collect);

            if (collect.size() == 0) {
                throw new ValidationException("测试失败");
            } else {
                EmotionVO emotionVO = collect.get(0);
                MoodType key = emotionVO.getKey();
                List<GameCopywriting> gameCopywritingList = gameCopywritingDao.findByMoodTypeAndDeletedIsFalse(key).stream().limit(1).collect(Collectors.toList());
                if (gameCopywritingList.size() == 0) {
                    return new GameCopywriting();
                } else {
                    return gameCopywritingList.get(0);
                }

            }
        }

    }

    public static void main(String[] args) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_key", "fdsf");
        jsonObject.addProperty("api_secret", "fdsaf");
        jsonObject.addProperty("image_url", "fdsad");
        String s = jsonObject.toString();
        System.out.println(s);
    }
}
