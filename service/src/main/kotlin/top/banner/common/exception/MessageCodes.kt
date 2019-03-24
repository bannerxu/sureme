package top.banner.common.exception


object MessageCodes {

    const val AUTH_TOKEN = "auth.token"//accesstoken 错误
    const val AUTH_TOKEN_EMPTY = "auth.token.empty"//token为空
    const val AUTH_USERNAME_DUPLICATE = "auth.username.duplicate"//用户名重复
    const val AUTH_PASSWORD = "auth.error"//用户名或密码错误
    const val AUTH_PERMISSION = "auth.permission"//权限不足
    const val AUTH_ACCOUNT_PASSWORD_WRONG = "auth.account.password.wrong"//账号或密码错误


    const val REQUEST_ARGUMENT = "request.argument"//请求参数错误
    const val INTERNAL_SERVER_ERROR = "server.internal"

    //商家
    const val ADMIN_IS_NOT_EXIST = "admin.is.not.exist"
    //管理员
    const val MANAGER_ACCOUNT_IS_EXIST = "manager.account.is.exist"//管理员账号已经存在
    const val MANAGER_IS_NOT_EXIST = "manager.is.not.exist"

    const val MANAGER_PASSWORD_IS_FAIL = "manager.password.is.fail"


    //验证码
    const val AUTH_PHOCAPTCHA_SEND_FAIL = "auth.phocaptcha.send.fail"//用户不存在
    const val AUTH_PICCAPTCHA_LOST = "auth.captcha.lost"//验证码已失效
    const val AUTH_PICCAPTCHA_WRONG = "auth.captcha.wrong"//验证码错误


    const val USER_ID_EMPTY = "user.id.empty"
    const val OLD_USER_EMPTY = "old.user.empty"
    const val CMS_ID_EMPTY = "cms.id.empty"
    const val CMS_ARTICLE_NOT_EXIST = "cms.article_not_exist"
    const val ARTICLE_ID_EMPTY = "article.id.empty"

    const val CMS_COMMODITY_NOT_EXIST = "cms.commodity.not.exist"
    const val CMS_COMMODITY_BANNER_NOT_EXIST = "cms.commodity.banner.not.exist"
    const val CMS_STOCK_KEEPING_UNIT_NOT_EXIST = "cms.stock.keeping.unit.not.exist"
    const val CMS_COUPON_NOT_EXIST = "cms.coupon.not.exist"
    const val CMS_BANNER_ERROR = "cms.banner.error"
    const val CMS_ORDER_NOT_EXIST = "cms.order.not.exist"
    const val CMS_GROUP_BUYING_NOT_EXIST = "web.group.buying.not.exist"
    const val CMS_CATEGORY_NOT_EXIST = "cms.category.not.exist"
    const val WEB_ADDRESS_NOT_EXIST = "web.address.not.exist"
    const val WEB_COMMODITY_NOT_EXIST = "web.commodity.not.exist"
    const val WEB_USER_NOT_EXIST = "web.user.not.exist"
    const val WEB_ORDER_NOT_EXIST = "web.order.not.exist"
    const val SHAREITEM_IS_EXIST = "shareItem.is.exist"
    const val WEB_GROUP_BUYING_NOT_EXIST = "web.group.buying.not.exist"
    const val WEB_SKU_NOT_EXIST = "web.sku.not.exist"
    const val WECHAT_AUTHORIZE_FAILE = "wechat.auth.fail"//微信授权失败
    const val WEB_CART_ITEM_NOT_EXIST = "web.cart.item.not.exist"
    const val WEB_COUPON_NOT_EXIST = "web.coupon.not.exist"
    const val WEB_COUPON_HAS_BEEN_PULLED = "web.coupon.has.been.pulled"
    const val WEB_COUPON_NOT_PULL_TIME = "web.coupon.not.pull.time"
    const val WEB_ARTICLE_NOT_EXIST = "web.article.not.exist"
    const val WEB_USER_COMMENT_EXIST = "web.user.comment.exist"
    const val WEB_COUPON_CAN_NOT_USE = "web.coupon.can.not.use"
    const val WEB_COUPON_NOT_ENOUGH = "web.coupon.not.enough"
    const val WEB_SKU_STOCK_NOT_ENOUGH = "web.sku.stock.not.enough"
    const val WEB_GROUP_BUYING_NOT_IN_TIME = "web.group.buying.not.in.time"
    const val WEB_ORDER_USER_NO_MATCH = "web.order.user.no.match"
    const val WEB_USER_GROUP_BUYING_PEOPLE_FULL = "web.user.group.buying.people.full"
    const val WEB_USER_GROUP_BUYING_NOT_EXIST = "web.user.group.buying.not.exist"
    const val CMS_SECOND_NOT_EXIST = "cms.second.not.exist"
    const val WEB_USER_GROUP_BUYING_OWN = "web.user.group.buying.own"
    const val WEB_SECOND_NOT_EXIST = "web.second.not.exist"
    const val WEB_SECOND_SOLD_OUT = "web.second.sold.out"
    const val WEB_ORDER_ITEMS_IS_NULL = "web.order.items.is.null"
    const val WEB_ORDER_REFUNDED_OR_RETURNED = "web.order.refunded.or.returned"
    const val CMS_APPLY_RECORD_NOT_EXIST = "cms.apply.record.not.exist"
    const val EXCEL_ERROR = "excel.error"
    const val USER_NOT_MATCH = "user.not.match"
    const val ORDER_STATUS_NOT_MATCH = "order.status.not.match"
}
