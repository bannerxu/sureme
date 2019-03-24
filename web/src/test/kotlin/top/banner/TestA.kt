package top.banner

import org.junit.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.junit.runner.RunWith
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import top.banner.common.exception.ValidationException


/**
 *
 * @author xgl
 */
@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class TestA {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun exampleTest() {
        this.mockMvc.perform(get("/")).andExpect(status().is2xxSuccessful)
                .andExpect(content().string("Hello"))

    }

}