package com.template.persistence

import com.template.config.security.user.Authority
import com.template.persistence.entity.UserEntity
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {

    @Query(
        """
        SELECT u FROM UserEntity u
        WHERE u.email LIKE %?1% OR u.username LIKE %?1% OR u.firstName LIKE %?1% OR u.lastName LIKE %?1%
    """,
    )
    fun search(query: String): List<UserEntity>

    @Query(
        """
        SELECT u FROM UserEntity u JOIN ApiKeyEntity a ON u.id = a.owner.id
        WHERE a.hashedKey = ?1
    """,
    )
    fun findByApiKey(key: String): UserEntity?

    @Modifying
    @Query(
        """
        UPDATE UserEntity u SET u.authorities = ?2
        WHERE u.id = ?1
    """,
    )
    fun updateById(userId: UUID, authorities: List<Authority>): UserEntity?
}
