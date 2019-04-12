package top.banner.common.mvc

import top.banner.common.annotation.AllOpenClass
import top.banner.common.annotation.NoArgsConstructor


/**
 *
 * @author xgl
 */
@NoArgsConstructor
@AllOpenClass
data class RequestDevice(
        val ip: String,
        val version: String? = null
){

}