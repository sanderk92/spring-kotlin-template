package com.template.security.user

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.context.ApplicationContextException
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

sealed interface UserAuthority {
    fun role(): String

    object READ : UserAuthority {
        const val role = "ROLE_READ"
        override fun role() = role;
    }

    object WRITE : UserAuthority {
        const val role = "ROLE_WRITE"
        override fun role() = role;
    }

    object DELETE : UserAuthority {
        const val role = "ROLE_DELETE"
        override fun role() = role;
    }

    object ADMIN : UserAuthority {
        const val role = "ROLE_ADMIN"
        override fun role() = role;
    }

    companion object {
        fun valueOf(role: String?): UserAuthority? = when (role) {
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
    override fun convert(source: String): UserAuthority? {
        return UserAuthority.valueOf("ROLE_${source}")
            ?: throw ApplicationContextException("UserAuthority '$source' does not exist")
    }
}

class UserAuthorityJsonSerializer : StdSerializer<UserAuthority>(UserAuthority::class.java) {
    override fun serialize(value: UserAuthority, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value::class.simpleName)
    }
}
