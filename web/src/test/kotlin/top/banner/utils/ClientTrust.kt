package top.banner.utils

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

/**
 * @author CJ
 */
object ClientTrust {
    fun <T : MockHttpServletRequestBuilder> autoTrust(builder: T, uri: String
                                                      , agent: String = "v1.0")
            : T {

        return builder
                .header("User-Agent", agent) as T
    }

}