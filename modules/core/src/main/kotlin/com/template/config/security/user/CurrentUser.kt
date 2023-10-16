package com.template.config.security.user

import java.util.*

/**
 * Customizable representation of the current user regardless of auth method
 */
internal class CurrentUser private constructor(
    val id: UUID,
    val authorities: List<Authority>,
)
