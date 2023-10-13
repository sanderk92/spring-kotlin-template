package com.template.persistence

import com.template.persistence.model.ApiKeyEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ApiKeyRepository : JpaRepository<ApiKeyEntity, UUID> {

    @Modifying
    @Query(
        """
        DELETE FROM ApiKeyEntity a
        WHERE a.owner.id = ?1 AND a.id = ?2
     """
    )
    fun delete(userId: UUID, apikeyId: UUID)
}
