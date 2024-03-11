package com.marsofandrew.bookkeeper.user.access.repository

import com.marsofandrew.bookkeeper.user.access.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface UserRepository: JpaRepository<UserEntity, Long>