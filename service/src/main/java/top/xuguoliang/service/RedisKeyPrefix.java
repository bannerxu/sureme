package top.xuguoliang.service;

/**
 * @author
 */
public class RedisKeyPrefix {
    public static String cmsAuthIdToToken(Integer userId) {
        return "cms.uid.token:" + userId;
    }

    public static String cmsAuthTokenToId(String token) {
        return "cms.token.uid:" + token;
    }

    public static String cmsAuthIdToRole(Integer userId) {
        return "cms.uid.role:" + userId;
    }

    public static String webAuthIdToToken(Integer userId) {
        return "web.uid.token:" + userId;
    }

    public static String webAuthTokenToId(String token) {
        return "web.token.uid:" + token;
    }


    public static String captchaUserId(Integer userId) {
        return "captchaUserId.uid:" + userId;
    }

    public static String buildPhoneToCaptcha(String phone) {
        return "auth.captcha.phone:" + phone;
    }

    public static String phoneCaptchaUser(String phone) {
        return "user.phone.captcha:" + phone;
    }

    public static String captcha(String phone) {
        return "auth.captcha:" + phone;
    }

    public static String buildTokenToUserId(String token) {
        return "auth.token.uid:" + token;
    }


    public static String buildTokenToManagerId(String token) {
        return "manager.token.uid:" + token;
    }


    public static String userQuestionHistory(Integer userId) {
        return "user.question.history:" + userId;
    }

    public static String WebWXAccessToken() {
        return "web.wx.accessToken";
    }

    public static String secondFindPage() {
        return "secondFindPage";
    }
}

