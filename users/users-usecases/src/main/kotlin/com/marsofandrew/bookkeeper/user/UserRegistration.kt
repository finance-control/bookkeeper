package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.properties.id.NumericId

interface UserRegistration {

    fun register(unregisteredUser: UnregisteredUser): NumericId<User>
}