package com.template.persistence

import com.template.persistence.entity.ApiKeyEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface ApiKeyRepository : JpaRepository<ApiKeyEntity, UUID> {
    @Modifying
    @Query("DELETE FROM ApiKeyEntity a WHERE a.owner.id = :userId AND a.id = :apiKeyId")
    fun delete(userId: UUID, apiKeyId: UUID)
}
