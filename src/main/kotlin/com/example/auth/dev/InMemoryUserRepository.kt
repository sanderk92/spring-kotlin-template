package com.example.auth.dev

import com.example.auth.AuthenticatedUser
import com.example.auth.AuthenticatedUserRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.*

@Service
@ConditionalOnProperty("feature.in-memory-users")
class InMemoryUserRepository : AuthenticatedUserRepository {

    private val users = mutableListOf<AuthenticatedUser>()

    fun add(user: InMemoryUser): InMemoryUser =
        user.also(users::add)

    override fun create(subject: String): AuthenticatedUser =
        InMemoryUser(
            id = UUID.randomUUID(),
            subject = subject,
            apiKey = emptyList(),
        ).let(::add)

    override fun findByApiKey(apiKey: String): Optional<AuthenticatedUser> =
        Optional.ofNullable(users.firstOrNull { user -> user.apiKey.any { it.key == apiKey } })

    override fun findBySubject(subject: String): Optional<AuthenticatedUser> =
        Optional.ofNullable(users.firstOrNull { user -> user.subject == subject })
}