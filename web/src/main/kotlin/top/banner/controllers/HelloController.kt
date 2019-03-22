package top.banner.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


/**
 *
 * @author xgl
 */
@RestController
class HelloController {
    @GetMapping
    fun hello(): String = "Hello"
}