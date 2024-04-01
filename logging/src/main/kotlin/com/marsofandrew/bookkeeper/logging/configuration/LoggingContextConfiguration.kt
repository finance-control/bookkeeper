package com.marsofandrew.bookkeeper.logging.configuration

import com.marsofandrew.bookkeeper.logging.filter.RequestIdFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter


@Configuration
internal class LoggingContextConfiguration(
    private val requestIdFilter: RequestIdFilter
) {

    @Bean
    fun servletRegistrationBean(): FilterRegistrationBean<RequestIdFilter> {
        val filterRegistrationBean: FilterRegistrationBean<RequestIdFilter> = FilterRegistrationBean<RequestIdFilter>()
        filterRegistrationBean.filter = requestIdFilter
        filterRegistrationBean.order = 2
        return filterRegistrationBean
    }

    @Bean
    fun logFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        return filter
    }
}