package top.banner.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import top.banner.common.mvc.RequestDevice


/**
 *
 * @author xgl
 */
@RestController
class HelloController {
    @GetMapping
    fun hello(device: RequestDevice): String {
        return device.version!!
    }
}