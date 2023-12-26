package com.template.persistence

import com.template.persistence.entity.UserEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface UserRepository : JpaRepository<UserEntity, UUID> {
    @Query(
        """
        SELECT u FROM UserEntity u
        WHERE u.email LIKE :query% OR u.username LIKE :query% OR u.firstName LIKE :query% OR u.lastName LIKE :query%
    """,
    )
    fun search(query: String): List<UserEntity>

    @Query(
        """
        SELECT u FROM UserEntity u JOIN ApiKeyEntity a ON u.id = a.owner.id
        WHERE a.hashedKey = :key
    """,
    )
    fun findByApiKey(key: String): UserEntity?
}
