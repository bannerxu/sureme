package top.banner.models.gamecopywriting;

/**
 * @description: 情绪分类
 * @author: XGL
 * @create: 2018-08-29 10:38
 **/
public enum MoodType {

    /**
     * 愤怒
     */
    anger("anger"),
    /**
     * 厌恶
     */
    disgust("disgust"),
    /**
     * 恐惧
     */
    fear("fear"),
    /**
     * 高兴
     */
    happiness("happiness"),
    /**
     * 平静
     */
    neutral("neutral"),
    /**
     * 伤心
     */
    sadness("sadness"),
    /**
     * 惊讶
     */
    surprise("surprise");

    String type;

    MoodType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MoodType{" +
                "type='" + type + '\'' +
                '}';
    }
}
