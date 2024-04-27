package com.template.persistence

import com.template.persistence.entity.ApiKeyEntity
import com.template.persistence.graphs.CompleteApiKey
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface ApiKeyRepository : JpaRepository<ApiKeyEntity, UUID> {
    @CompleteApiKey
    @Query("SELECT a FROM ApiKeyEntity a WHERE a.hashedKey = :hashedKey")
    fun findByApiKey(hashedKey: String): ApiKeyEntity?

    @CompleteApiKey
    @Query("SELECT a FROM ApiKeyEntity a WHERE a.owner.id = :userId")
    fun findAllByUserId(userId: UUID): List<ApiKeyEntity>

    @Modifying
    @Query("DELETE FROM ApiKeyEntity a WHERE a.id = :apiKeyId AND a.owner.id = :userId")
    fun delete(userId: UUID, apiKeyId: UUID)
}
