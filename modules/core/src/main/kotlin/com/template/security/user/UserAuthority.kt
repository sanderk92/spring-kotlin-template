package com.template.security.user

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.context.ApplicationContextException
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

sealed interface UserAuthority {
    fun role(): String
    override fun toString(): String

    object READ : UserAuthority {
        const val role = "ROLE_READ"
        override fun role() = role;
        override fun toString(): String = this.javaClass.simpleName
    }

    object WRITE : UserAuthority {
        const val role = "ROLE_WRITE"
        override fun role() = role;
        override fun toString(): String = this.javaClass.simpleName
    }

    object DELETE : UserAuthority {
        const val role = "ROLE_DELETE"
        override fun role() = role;
        override fun toString(): String = this.javaClass.simpleName
    }

    object ADMIN : UserAuthority {
        const val role = "ROLE_ADMIN"
        override fun role() = role;
        override fun toString(): String = this.javaClass.simpleName
    }

    companion object {
        fun valueOf(string: String?): UserAuthority? = when (string) {
            READ.toString() -> READ
            WRITE.toString() -> WRITE
            DELETE.toString() -> DELETE
            ADMIN.toString() -> ADMIN
            else -> null
        }

        fun valueOfRole(role: String?): UserAuthority? = when (role) {
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
class UserAuthorityPropertyConverter : Converter<String, UserAuthority> {
    override fun convert(source: String): UserAuthority? =
        UserAuthority.valueOf(source) ?: throw ApplicationContextException("Invalid UserAuthority '$source'")
}
