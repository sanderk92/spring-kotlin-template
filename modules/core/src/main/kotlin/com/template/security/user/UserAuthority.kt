package com.template.security.user

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.context.ApplicationContextException
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

sealed interface UserAuthority {
    fun name(): String
    fun value(): String

    object READ : UserAuthority {
        const val value = "ROLE_READ"
        override fun value() = value;
        override fun name(): String = "READ"
    }

    object WRITE : UserAuthority {
        const val value = "ROLE_WRITE"
        override fun value() = value;
        override fun name(): String = "WRITE"
    }

    object DELETE : UserAuthority {
        const val value = "ROLE_DELETE"
        override fun value() = value;
        override fun name(): String = "DELETE"
    }

    object ADMIN : UserAuthority {
        const val value = "ROLE_ADMIN"
        override fun value() = value;
        override fun name(): String = "ADMIN"
    }

    companion object {
        fun valueOf(role: String?): UserAuthority? = when (role) {
            READ.value -> READ
            WRITE.value -> WRITE
            DELETE.value -> DELETE
            ADMIN.value -> ADMIN
            else -> null
        }
    }
}

@Component
@ConfigurationPropertiesBinding
class UserAuthorityConverter : Converter<String, UserAuthority> {
    override fun convert(source: String): UserAuthority? {
        return UserAuthority.valueOf("ROLE_${source}")
            ?: throw ApplicationContextException("UserAuthority '$source' does not exist")
    }
}
