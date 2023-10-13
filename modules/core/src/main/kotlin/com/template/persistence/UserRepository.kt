package com.template.persistence

import com.template.persistence.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*
import org.springframework.data.jpa.repository.Modifying

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
        WHERE a.key = ?1
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
    fun updateById(userId: UUID, authorities: List<String>): UserEntity?
}
