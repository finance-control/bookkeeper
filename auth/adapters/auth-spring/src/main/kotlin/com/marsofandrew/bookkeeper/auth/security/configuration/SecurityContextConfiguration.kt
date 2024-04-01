package com.marsofandrew.bookkeeper.auth.security.configuration

import com.marsofandrew.bookkeeper.auth.security.exception.AuthExceptionHandler
import com.marsofandrew.bookkeeper.auth.security.filter.AuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.CrossOriginOpenerPolicyConfig
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
internal class SecurityContextConfiguration(
    private val authenticationProviders: List<AuthenticationProvider>,
    private val authenticationFilters: List<AuthenticationFilter>,
) {

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun apiFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity.csrf { it.disable() }
        httpSecurity.cors {
            val configuration = CorsConfiguration()
            configuration.allowedOrigins = mutableListOf("*")
            configuration.allowedMethods = mutableListOf("*")
            configuration.allowedHeaders = mutableListOf("*")
            val source = UrlBasedCorsConfigurationSource()
            source.registerCorsConfiguration("/**", configuration)
            it.configurationSource(source)
        }
        httpSecurity.logout { it.disable() }

        authenticationProviders.forEach {
            httpSecurity.authenticationProvider(it)
        }
        authenticationFilters.forEach { filter ->
            httpSecurity.addFilterBefore(filter, AnonymousAuthenticationFilter::class.java)
        }


        httpSecurity.sessionManagement { sessionManagement ->
            sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )
        }

        httpSecurity.authorizeRequests { authHttpRequests ->
            authHttpRequests
                .requestMatchers(
                    "/api/v1/users/registration",
                    "/swagger-ui/**",
                    "/api/openapi.json",
                    "/api/openapi.json/**",
                    "/api/v1/assets/currency"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
        }

        httpSecurity.exceptionHandling {
            it.authenticationEntryPoint(AuthExceptionHandler())
        }

        return httpSecurity.build()
    }
}
