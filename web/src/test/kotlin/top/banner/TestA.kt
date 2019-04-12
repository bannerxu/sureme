package top.banner

import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import top.banner.utils.MockDevice


/**
 *
 * @author xgl
 */

class TestA : BaseMVCTest() {
    @Test
    fun exampleTest() {
        mockMvc.perform(
                clientGet(uri = "/", device = MockDevice.iOS1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andDo(print()).andExpect(MockMvcResultMatchers.content().string(MockDevice.iOS1.agent))

    }
}
