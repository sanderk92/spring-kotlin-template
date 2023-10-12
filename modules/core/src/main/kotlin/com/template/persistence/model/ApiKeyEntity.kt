package com.template.persistence.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "api-keys")
class ApiKeyEntity(
    @Id
    val id: UUID,

    @Column(name = "hashed-key", nullable = false, unique = true)
    val key: String,

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
