package com.chagu.splitbill.repository

import com.chagu.splitbill.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Int> {

    fun findByEmail(email: String): UserEntity?
}