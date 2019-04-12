package top.banner.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import top.banner.common.mvc.RequestDeviceResolver


/**
 *
 * @author xgl
 */
@Configuration
open class WebConfig : WebMvcConfigurationSupport() {
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(RequestDeviceResolver())
    }

}