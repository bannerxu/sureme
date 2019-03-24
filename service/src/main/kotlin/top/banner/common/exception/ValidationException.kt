package top.banner.common.exception

/**
 * 正常业务层面验证数据或服务时，抛出的的ValidationException会被转换成Result返回
 */
class ValidationException(
        val code: String? = "参数异常",
        val msg: String? = "参数异常",
        val args: Array<Any>? = null
) : RuntimeException()