package com.template.persistence.entity

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.*

/**
 * This superclass allows us to use generated properties, while not having to deal with nullable fields.
 * It provides predictable default values when not yet persisted, and contains actual values once persisted.
 * Optionally, generated fields can be overwritten by the subclass by passing them to the constructor.
 */
@MappedSuperclass
abstract class BaseEntity(
    @Id
    open val id: UUID,
) {
    constructor() : this(UUID(0, 0))
}