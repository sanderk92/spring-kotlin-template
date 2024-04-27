package com.template.persistence.entity

import com.template.config.security.user.Authority
import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "apikeys")
internal data class ApiKeyEntity(
    @Id
    val id: UUID,
    @Size(max = 256)
    @Column(name = "hashed_key", nullable = false, unique = true)
    val hashedKey: String,
    @Size(max = 256)
    @Column(name = "name", nullable = false)
    val name: String,
    @JoinColumn(name = "owner")
    @ManyToOne(cascade = [CascadeType.DETACH])
    val owner: UserEntity,
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "apikey_authorities")
    @Column(name = "authorities", nullable = false)
    val authorities: Set<Authority>,
) {
    override fun equals(other: Any?): Boolean =
        this === other || other is ApiKeyEntity &&
            id == other.id &&
            hashedKey == other.hashedKey &&
            name == other.name &&
            owner.id == other.owner.id &&
            authorities == other.authorities

    override fun hashCode(): Int =
        31 * id.hashCode() +
            31 * hashedKey.hashCode() +
            31 * name.hashCode() +
            31 * owner.id.hashCode() +
            31 * authorities.hashCode()

    override fun toString(): String = "${this.javaClass.simpleName}(id=$id)"
}
