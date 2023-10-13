package com.template.persistence.model

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.*

/**
 * This superclass allows us to use generated properties, while not having to deal with nullable fields.
 * It provides default values while not yet persisted, and contains actual values when persisted
 */
@MappedSuperclass
abstract class BaseEntity(
    @Id
    val id: UUID,
) {
    constructor() : this(UUID(0, 0))
}
