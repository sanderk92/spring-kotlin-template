package com.template.persistence.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.*

@Entity
@Table(name = "api-keys")
class ApiKeyEntity(
    @Id
    val id: UUID,

    @Size(max = 256)
    @Column(name = "hashed-key", nullable = false, unique = true)
    val key: String,

    @Size(max = 256)
    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @Column(name = "read", nullable = false)
    val read: Boolean,

    @Column(name = "write", nullable = false)
    val write: Boolean,

    @Column(name = "delete", nullable = false)
    val delete: Boolean,

    @JoinColumn(name = "owner")
    @ManyToOne(cascade = [CascadeType.DETACH])
    val owner: UserEntity?,
)
