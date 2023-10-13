package com.template.config.security.user

const val READ_ROLE = "ROLE_READ"
const val WRITE_ROLE = "ROLE_WRITE"
const val DELETE_ROLE = "ROLE_DELETE"
const val ADMIN_ROLE = "ROLE_ADMIN"

enum class Authority(val role: String) {
    READ(READ_ROLE),
    WRITE(WRITE_ROLE),
    DELETE(DELETE_ROLE),
    ADMIN(ADMIN_ROLE);

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
