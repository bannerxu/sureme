package top.banner

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultHandler
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import top.banner.common.annotation.AllOpenClass
import top.banner.entity.User
import top.banner.utils.ClientTrust
import top.banner.utils.MockDevice


/**
 *
 * @author xgl
 */

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@AllOpenClass
open class BaseMVCTest {

    @Autowired
    lateinit var mockMvc: MockMvc


    fun print(): ResultHandler {
        return MockMvcResultHandlers.print()
    }


    /**
     * client 方式的get
     */
    protected fun clientGet(uri: String, device: MockDevice? = null, user: User? = null): MockHttpServletRequestBuilder {
        return clientSupport(MockMvcRequestBuilders.get(uri), uri, user, device)
    }



    private fun <T : MockHttpServletRequestBuilder> clientSupport(initBuilder: T, uri: String, user: User?, device: MockDevice?)
            : T {

        if (device != null) {
            return device.use(initBuilder, uri)
        }
        return ClientTrust.autoTrust(initBuilder, uri)
    }


}