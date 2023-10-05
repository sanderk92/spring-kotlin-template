package com.template.security.apikey

import java.util.*

interface ApiKey {
    val id: UUID
    val key: String
    val name: String
    val read: Boolean
    val write: Boolean
    val delete: Boolean
}
