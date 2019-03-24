package top.banner.common.exception

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import top.banner.common.Result
import java.util.*


@RestControllerAdvice
class GlobalControllerExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler::class.java)

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException::class)
    fun handleBaseException(e: ValidationException): Result {
        var desc = applicationContext.getMessage(e.code!!, e.args, e.msg, Locale.getDefault())
        if (StringUtils.isEmpty(desc)) {
            logger.info("can not find desc of collector:" + e.code)
            desc = e.code
        }
        return Result(e.code, desc!!)
    }


}

