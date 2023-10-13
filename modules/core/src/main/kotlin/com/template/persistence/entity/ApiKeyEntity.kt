package com.template.persistence.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "api-keys")
class ApiKeyEntity(
    @Size(max = 256)
    @Column(name = "hashed-key", nullable = false, unique = true)
    val hashedKey: String,

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
