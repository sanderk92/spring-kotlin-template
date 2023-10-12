package com.template.persistence.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    val id: UUID,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "username", nullable = false, unique = true)
    val username: String,

    @Column(name = "firstname", nullable = false)
    val firstName: String,

    @Column(name = "lastname", nullable = false)
    val lastName: String,

    @Column(name = "api-keys", nullable = false)
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
    val apiKeys: List<ApiKeyEntity>,

    @ElementCollection
    @Column(name = "authorities", nullable = false)
    val authorities: List<String>
)
