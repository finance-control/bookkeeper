package com.marsofandrew.bookkeeper.auth.security.configuration

import com.marsofandrew.bookkeeper.auth.security.filter.AuthenticationFilter
import com.marsofandrew.bookkeeper.auth.security.exception.AuthExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter

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
        httpSecurity.cors { it.disable() }
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
                .anyRequest().authenticated()
        }

        httpSecurity.exceptionHandling{
            it.authenticationEntryPoint(AuthExceptionHandler())
        }

        return httpSecurity.build()
    }
}