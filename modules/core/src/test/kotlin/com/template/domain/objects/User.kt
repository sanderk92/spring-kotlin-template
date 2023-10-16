package com.template.domain.objects

import com.template.domain.model.User
import java.util.*

const val PRINCIPAL_NAME = "ef2db5bb-b7a0-4ff3-99dc-a7d95dc1e84c"

internal val user = User(
    id = UUID.fromString(PRINCIPAL_NAME),
    email = "email",
    username = "username",
    firstName = "firstName",
    lastName = "lastName",
    apiKeys = listOf(apiKey),
)
