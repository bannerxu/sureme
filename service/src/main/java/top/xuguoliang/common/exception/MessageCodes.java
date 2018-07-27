package top.xuguoliang.common.exception;


public class MessageCodes {

    public final static String AUTH_TOKEN = "auth.token";//accesstoken 错误
    public final static String AUTH_TOKEN_EMPTY = "auth.token.empty";//token为空
    public final static String AUTH_USERNAME_DUPLICATE = "auth.username.duplicate";//用户名重复
    public final static String AUTH_PASSWORD = "auth.error";//用户名或密码错误
    public final static String AUTH_PERMISSION = "auth.permission";//权限不足
    public final static String AUTH_ACCOUNT_PASSWORD_WRONG = "auth.account.password.wrong";//账号或密码错误


    public final static String REQUEST_ARGUMENT = "request.argument";//请求参数错误
    public final static String INTERNAL_SERVER_ERROR = "server.internal";

    //商家
    public final static String ADMIN_IS_NOT_EXIST = "admin.is.not.exist";
    //管理员
    public final static String MANAGER_ACCOUNT_IS_EXIST = "manager.account.is.exist";//管理员账号已经存在
    public final static String MANAGER_IS_NOT_EXIST = "manager.is.not.exist";

    public final static String MANAGER_PASSWORD_IS_FAIL = "manager.password.is.fail";


    //验证码
    public final static String AUTH_PHOCAPTCHA_SEND_FAIL = "auth.phocaptcha.send.fail";//用户不存在
    public final static String AUTH_PICCAPTCHA_LOST = "auth.captcha.lost";//验证码已失效
    public final static String AUTH_PICCAPTCHA_WRONG = "auth.captcha.wrong";//验证码错误


    public static final String USER_ID_EMPTY = "user.id.empty";
    public static final String OLD_USER_EMPTY = "old.user.empty";
    public static final String CMS_ID_EMPTY = "cms.id.empty";
}
