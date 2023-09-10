package com.example.security.user

import java.util.*

class CurrentUser private constructor(
    val id: UUID,
    val authorities: List<String>,
)