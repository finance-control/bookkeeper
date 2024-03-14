package com.marsofandrew.bookkeeper.auth.impl

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FakeUserAuthenticationImplTest {

    private lateinit var fakeUserAuthenticationImpl: FakeUserAuthenticationImpl

    @BeforeEach
    fun setup() {
        fakeUserAuthenticationImpl = FakeUserAuthenticationImpl()
    }

    @Test
    fun `getUserIdByAuth returns user id`() {
        fakeUserAuthenticationImpl.authenticate("55") shouldBe 55
    }
}