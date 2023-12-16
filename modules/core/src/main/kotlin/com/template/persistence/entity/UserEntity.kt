package com.template.persistence.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.*

@Entity
@Table(name = "users")
internal data class UserEntity(
    @Id
    val id: UUID,

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

    @Column(name = "api_keys", nullable = false)
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
    val apiKeys: List<ApiKeyEntity>,
) {
    override fun equals(other: Any?): Boolean =
        this === other || other is UserEntity &&
            id == other.id &&
            email == other.email &&
            username == other.username &&
            firstName == other.firstName &&
            lastName == other.lastName

    override fun hashCode(): Int =
        31 * id.hashCode() +
            31 * email.hashCode() +
            31 * username.hashCode() +
            31 * firstName.hashCode() +
            31 * lastName.hashCode()

    override fun toString(): String =
        "${this.javaClass.simpleName}(id=$id)"
}
