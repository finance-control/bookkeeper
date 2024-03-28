package com.marsofandrew.bookkeeper.user.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.access.entity.toUserEntity
import com.marsofandrew.bookkeeper.user.access.repository.UserRepository
import com.marsofandrew.bookkeeper.user.access.id.UserIdGenerator
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
internal class UserStorageImpl(
    private val userRepository: UserRepository,
    private val userIdGenerator: UserIdGenerator
) : UserStorage {
    override fun findById(id: NumericId<User>): User? {
        return userRepository.findById(id.value).getOrNull()?.toModel()
    }

    override fun create(user: User): User {
        require(!user.id.initialized) { "user has already has id" }

        return userRepository.saveAndFlush(user.copy(id = userIdGenerator.generateId()).toUserEntity()).toModel()
    }
}