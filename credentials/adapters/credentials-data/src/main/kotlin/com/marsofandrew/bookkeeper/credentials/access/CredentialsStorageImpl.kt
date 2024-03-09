package com.marsofandrew.bookkeeper.credentials.access

import com.marsofandrew.bookkeeper.credentials.UserCredentials
import com.marsofandrew.bookkeeper.credentials.email.Email
import com.marsofandrew.bookkeeper.credentials.entity.toCredentialsEntity
import com.marsofandrew.bookkeeper.credentials.repository.CredentialsRepository
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
internal class CredentialsStorageImpl(
    private val credentialsRepository: CredentialsRepository
) : CredentialsStorage {
    override fun findByUserId(userId: NumericId<User>): UserCredentials? {
        return credentialsRepository.findById(userId.value).getOrNull()?.toModel()
    }

    override fun findByEmail(email: Email): UserCredentials? = credentialsRepository.findByEmail(email.value)?.toModel()

    @Transactional
    override fun createOrUpdate(userCredentials: UserCredentials) {
        credentialsRepository.save(userCredentials.toCredentialsEntity())
    }
}