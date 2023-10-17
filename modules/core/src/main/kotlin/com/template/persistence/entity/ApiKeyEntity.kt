package com.template.persistence.entity

import com.template.config.security.user.Authority
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "apikeys")
internal data class ApiKeyEntity(

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
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "apikey-authorities")
    @Column(name = "authorities", nullable = false)
    val authorities: List<Authority>

) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ApiKeyEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "UserEntity(id=$id)"
    }
}
