package com.marsofandrew.bookkeeper.user.credentials

import com.marsofandrew.bookkeeper.credentials.UserEmailSelection
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.User
import org.springframework.stereotype.Service

@Service
internal class UserEmailSelectorImpl(
    private val userEmailSelection: UserEmailSelection
) : UserEmailSelector {

    override fun select(userId: NumericId<User>): Email {
        return userEmailSelection.select(userId.value.asId())
    }
}