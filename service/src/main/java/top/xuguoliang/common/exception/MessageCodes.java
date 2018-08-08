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
    public static final String CMS_ARTICLE_NOT_EXIST = "cms.article_not_exist";
    public static final String ARTICLE_ID_EMPTY = "article.id.empty";

    public static final String CMS_COMMODITY_NOT_EXIST = "cms.commodity.not.exist";
    public static final String CMS_COMMODITY_BANNER_NOT_EXIST = "cms.commodity.banner.not.exist";
    public static final String CMS_STOCK_KEEPING_UNIT_NOT_EXIST = "cms.stock.keeping.unit.not.exist";
    public static final String CMS_COUPON_NOT_EXIST = "cms.coupon.not.exist";
    public static final String CMS_BANNER_ERROR = "cms.banner.error";
    public static final String CMS_ORDER_NOT_EXIST = "cms.order.not.exist";
    public static final String CMS_GROUP_BUYING_NOT_EXIST = "web.group.buying.not.exist";
    public static final String CMS_CATEGORY_NOT_EXIST = "cms.category.not.exist";
    public static final String WEB_ADDRESS_NOT_EXIST = "web.address.not.exist";
    public static final String WEB_COMMODITY_NOT_EXIST = "web.commodity.not.exist";
    public static final String WEB_USER_NOT_EXIST = "web.user.not.exist";
    public static final String WEB_ORDER_NOT_EXIST = "web.order.not.exist";
    public static final String SHAREITEM_IS_EXIST = "shareItem.is.exist";
    public static final String WEB_GROUP_BUYING_NOT_EXIST = "web.group.buying.not.exist";
    public static final String WEB_SKU_NOT_EXIST = "web.sku.not.exist";
    public static final String WECHAT_AUTHORIZE_FAILE = "wechat.auth.fail";//微信授权失败
}
