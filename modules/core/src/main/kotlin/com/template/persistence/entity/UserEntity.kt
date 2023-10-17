package com.template.persistence.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.*

@Entity
@Table(name = "users")
internal data class UserEntity(

    override val id: UUID,

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

) : BaseEntity(id) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "UserEntity(id=$id)"
    }
}
