package com.marsofandrew.bookkeeper.auth.security.filter

import com.marsofandrew.bookkeeper.auth.security.authentication.FakeAuthentication
import com.marsofandrew.bookkeeper.auth.security.util.FAKE_HEADER
import com.marsofandrew.bookkeeper.auth.security.util.USER_ID_HEADER
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
internal class FakeAuthenticationFilter : AbstractAuthenticationFilter() {

    override fun getAuthentication(request: HttpServletRequest): Authentication? {
        val userId = request.userId

        return when {
            request.isFake && userId != null -> FakeAuthentication(userId)
            else -> null
        }
    }

    private val HttpServletRequest.isFake: Boolean
        get() = getHeader(FAKE_HEADER).toBoolean()

    private val HttpServletRequest.userId: String?
        get() = getHeader(USER_ID_HEADER)
}
