package top.banner.common.mvc

import org.springframework.core.MethodParameter
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import top.banner.common.utils.ServletUtils
import javax.servlet.http.HttpServletRequest


/**
 * 参数处理
 * @author xgl
 */
@Component
class RequestDeviceResolver : HandlerMethodArgumentResolver {

    companion object {
        fun requestDevice(webRequest: NativeWebRequest, n: Boolean): RequestDevice? {
            val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            val ip = ServletUtils.clientIpAddress(request!!)
            // 分析 agent
            val agent = webRequest.getHeader("User-Agent") ?: return RequestDevice(ip)
            return RequestDevice(ip = ip, version = agent)
        }
    }

    override fun supportsParameter(parameter: MethodParameter?): Boolean = parameter != null
            && parameter.parameterType == RequestDevice::class.java

    override fun resolveArgument(parameter: MethodParameter?, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest?, binderFactory: WebDataBinderFactory?): RequestDevice? {
        if (webRequest == null) {
            throw IllegalStateException("webRequest should never null")
        }
        val n = parameter!!.hasParameterAnnotation(Nullable::class.java)

        return requestDevice(webRequest, n)
    }
}