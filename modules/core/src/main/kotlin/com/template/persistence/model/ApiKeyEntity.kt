package com.template.persistence.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.*

@Entity
@Table(name = "api-keys")
class ApiKeyEntity(
    @Size(max = 256)
    @Column(name = "hashed-key", nullable = false, unique = true)
    val key: String,

    @Size(max = 256)
    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @JoinColumn(name = "owner")
    @ManyToOne(cascade = [CascadeType.DETACH])
    val owner: UserEntity?,

    @ElementCollection
    @Column(name = "authorities", nullable = false)
    val authorities: List<String>
) : BaseEntity()
