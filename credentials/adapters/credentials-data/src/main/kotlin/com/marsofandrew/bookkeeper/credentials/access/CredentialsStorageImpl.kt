package com.marsofandrew.bookkeeper.credentials.access

import com.marsofandrew.bookkeeper.credentials.UserCredentials
import com.marsofandrew.bookkeeper.credentials.entity.toCredentialsEntity
import com.marsofandrew.bookkeeper.credentials.repository.CredentialsRepository
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.NumericId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
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

    override fun existByEmailAndNotOwnsById(email: Email, userId: NumericId<User>?): Boolean {
        return if (userId == null) {
            credentialsRepository.existsByEmail(email.value)
        } else {
            credentialsRepository.existsByEmailAndUserIdNot(email.value, userId.value)
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun createOrUpdate(userCredentials: UserCredentials) {
        credentialsRepository.saveAndFlush(userCredentials.toCredentialsEntity())
    }
}
