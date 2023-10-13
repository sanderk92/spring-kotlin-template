package com.template.config.security.user

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.context.ApplicationContextException
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

sealed interface Authority {
    fun role(): String
    override fun toString(): String

    object READ : Authority {
        const val role = "ROLE_READ"
        override fun role() = role
        override fun toString(): String = this.javaClass.simpleName
    }

    object WRITE : Authority {
        const val role = "ROLE_WRITE"
        override fun role() = role
        override fun toString(): String = this.javaClass.simpleName
    }

    object DELETE : Authority {
        const val role = "ROLE_DELETE"
        override fun role() = role
        override fun toString(): String = this.javaClass.simpleName
    }

    object ADMIN : Authority {
        const val role = "ROLE_ADMIN"
        override fun role() = role
        override fun toString(): String = this.javaClass.simpleName
    }

    companion object {
        fun valueOf(string: String?): Authority? = when (string) {
            READ.toString() -> READ
            WRITE.toString() -> WRITE
            DELETE.toString() -> DELETE
            ADMIN.toString() -> ADMIN
            else -> null
        }

        fun valueOfRole(role: String?): Authority? = when (role) {
            READ.role -> READ
            WRITE.role -> WRITE
            DELETE.role -> DELETE
            ADMIN.role -> ADMIN
            else -> null
        }
    }
}

@Component
@ConfigurationPropertiesBinding
class UserAuthorityPropertyConverter : Converter<String, Authority> {
    override fun convert(source: String): Authority? =
        Authority.valueOf(source) ?: throw ApplicationContextException("Invalid UserAuthority '$source'")
}
