package com.template.persistence

import com.template.persistence.model.ApiKeyEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApiKeyRepository : JpaRepository<ApiKeyEntity, UUID> {
}
