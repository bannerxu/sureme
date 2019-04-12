package top.banner.utils

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

/**
 * 伪造设备
 * @author CJ
 */
data class MockDevice(
        val agent: String
) {
    //

    fun <T : MockHttpServletRequestBuilder> use(builder: T, uri: String): T {
        return ClientTrust.autoTrust(builder, uri, agent) as T
    }

    companion object {
        val iOS1 = MockDevice("v1.1.0")
        val iOS2 = MockDevice("v1.1.0")
        val android1 = MockDevice("v1.1.0")
    }

}