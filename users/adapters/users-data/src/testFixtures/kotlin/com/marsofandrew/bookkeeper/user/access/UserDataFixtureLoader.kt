package com.marsofandrew.bookkeeper.user.access

import com.marsofandrew.bookkeeper.user.access.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserDataFixtureLoader {

    @Autowired
    private lateinit var userRepository: UserRepository


}