package com.template.security.user

enum class UserAuthority(val role: String) {
    READ("ROLE_READ"),
    WRITE("ROLE_WRITE"),
    DELETE("ROLE_DELETE"),
    ADMIN("ROLE_ADMIN");

    companion object {
        fun valueOfRole(role: String): UserAuthority? = values().firstOrNull { it.role == role }
    }
}
