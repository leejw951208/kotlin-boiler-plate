package demo.kotlinboilerplate.common.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

object SecurityUrl {
    val PUBLIC_URL_ARRAY = arrayOf("/auth/join", "/auth/login", "/docs", "/docs/**", "/swagger-ui/**")
    val ROLE_USER_URL_ARRAY = arrayOf("/user/**")
    val ROLE_ADMIN_URL_ARRAY = arrayOf("/admin/**")
}
