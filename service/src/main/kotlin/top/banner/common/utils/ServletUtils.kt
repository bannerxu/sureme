package top.banner.common.utils

import javax.servlet.http.HttpServletRequest


/**
 *
 * @author xgl
 */
open class ServletUtils {
    companion object {
        /**
         * 从请求中获取客户端IP地址
         * https://zh.wikipedia.org/wiki/X-Forwarded-For
         *
         * @param request 请求
         * @return ip
         */
        fun clientIpAddress(request: HttpServletRequest): String {
            // X-Forwarded-For: client1, proxy1, proxy2
            // 暂时表示信任这个来源
            val xff = request.getHeader("X-Forwarded-For")
            if (xff != null && xff.isNotEmpty()) {
                val ips = xff.trim { it <= ' ' }.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                // 应该确保最后一个 等同于 request.getRemoteAddr();
                return ips[0].trim { it <= ' ' }
            }
            return request.remoteAddr
        }
    }


}