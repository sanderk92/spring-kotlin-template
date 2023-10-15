package com.template.config.security.user

const val READ_ROLE = "ROLE_READ"
const val WRITE_ROLE = "ROLE_WRITE"
const val DELETE_ROLE = "ROLE_DELETE"
const val ADMIN_ROLE = "ROLE_ADMIN"

internal enum class Authority(val role: String) {
    READ(READ_ROLE),
    WRITE(WRITE_ROLE),
    DELETE(DELETE_ROLE),
    ADMIN(ADMIN_ROLE);

    companion object {
        private val lookup: Map<String, Authority> = Authority.values().associateBy(Authority::role)
        fun valueOfRole(role: String?): Authority? = role?.let(lookup::get)
    }
}
