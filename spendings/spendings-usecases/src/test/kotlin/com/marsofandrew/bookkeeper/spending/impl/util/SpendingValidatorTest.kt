package com.marsofandrew.bookkeeper.spending.impl.util

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.account.SpendingAccountValidator
import com.marsofandrew.bookkeeper.spending.category.SpendingCategoryValidator
import com.marsofandrew.bookkeeper.spending.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.spending.exception.InvalidCategoryException
import com.marsofandrew.bookkeeper.spending.fixture.spending
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class SpendingValidatorTest {

    private val spendingCategoryValidator = mockk<SpendingCategoryValidator>()
    private val spendingAccountValidator = mockk<SpendingAccountValidator>()

    private val validator = SpendingValidator(spendingCategoryValidator, spendingAccountValidator)

    @Test
    fun `validate throws exception  when spending category is invalid`() {
        val spending = spending(214.asId(), 54.asId())

        every { spendingCategoryValidator.validate(spending.userId, spending.categoryId) } returns false

        shouldThrowExactly<InvalidCategoryException> { validator.validate(spending) }
    }

    @Test
    fun `validate throws exception  when account is invalid`() {
        val spending = spending(214.asId(), 54.asId()){
            sourceAccountId = "45".asId()
        }

        every { spendingCategoryValidator.validate(spending.userId, spending.categoryId) } returns true
        every { spendingAccountValidator.validate(spending.userId, spending.sourceAccountId!!) } returns false

        shouldThrowExactly<InvalidAccountException> { validator.validate(spending) }
    }

    @Test
    fun `validate do nothing when account and spending category are invalid`() {
        val spending = spending(214.asId(), 54.asId()){
            sourceAccountId = "45".asId()
        }

        every { spendingCategoryValidator.validate(spending.userId, spending.categoryId) } returns true
        every { spendingAccountValidator.validate(spending.userId, spending.sourceAccountId!!) } returns true

        shouldNotThrowAny  {validator.validate(spending) }
    }
}