package com.example.security.user

import java.util.*

/**
 * Class containing the details of the currently authenticated user
 */
class CurrentUser private constructor(
    val id: UUID,
    val authorities: List<String>,
)