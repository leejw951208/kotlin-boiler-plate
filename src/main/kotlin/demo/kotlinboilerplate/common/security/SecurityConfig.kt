package demo.kotlinboilerplate.common.security

import com.fasterxml.jackson.databind.ObjectMapper
import demo.kotlinboilerplate.common.security.filter.JwtAuthenticationEntryPoint
import demo.kotlinboilerplate.common.token.TokenProperties
import demo.kotlinboilerplate.common.token.TokenProvider
import demo.kotlinboilerplate.common.security.filter.JwtAuthenticationFilter
import demo.kotlinboilerplate.common.security.filter.JwtExceptionFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher
import org.springframework.util.PathMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtProperties: TokenProperties,
    private val jwtProvider: TokenProvider,
    private val objectMapper: ObjectMapper
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity, introspector: HandlerMappingIntrospector, pathMatcher: PathMatcher): SecurityFilterChain {
        http
            .csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }
            .sessionManagement { session: SessionManagementConfigurer<HttpSecurity> -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .formLogin { formLogin: FormLoginConfigurer<HttpSecurity> -> formLogin.disable() }
            .httpBasic { httpBasic: HttpBasicConfigurer<HttpSecurity> -> httpBasic.disable() }
            .addFilterBefore(JwtAuthenticationFilter(jwtProperties, jwtProvider), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(JwtExceptionFilter(objectMapper), JwtAuthenticationFilter::class.java)
            .authorizeHttpRequests { authorize ->
                authorize.requestMatchers(
                    MvcRequestMatcher(introspector, "/join"),
                    MvcRequestMatcher(introspector, "/login"),
                    MvcRequestMatcher(introspector,"/jwt/**"),
                    MvcRequestMatcher(introspector,"/member/**")
                ).permitAll()
                authorize.anyRequest().authenticated()
            }
            .exceptionHandling { exception -> exception.authenticationEntryPoint(JwtAuthenticationEntryPoint()) }
        return http.build();
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}