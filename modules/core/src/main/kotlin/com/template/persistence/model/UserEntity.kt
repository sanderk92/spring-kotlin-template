package com.template.persistence.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.*

@Entity
@Table(name = "users")
class UserEntity(
    id: UUID,

    @Size(max = 256)
    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Size(max = 256)
    @Column(name = "username", nullable = false, unique = true)
    val username: String,

    @Size(max = 256)
    @Column(name = "firstname", nullable = false)
    val firstName: String,

    @Size(max = 256)
    @Column(name = "lastname", nullable = false)
    val lastName: String,

    @Column(name = "api-keys", nullable = false)
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
    val apiKeys: List<ApiKeyEntity>,

    @ElementCollection
    @Column(name = "authorities", nullable = false)
    val authorities: List<String>
) : BaseEntity(id)
