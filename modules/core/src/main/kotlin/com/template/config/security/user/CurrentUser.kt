package com.template.config.security.user

import java.util.*

class CurrentUser private constructor(
    val id: UUID,
    val authorities: List<Authority>,
)
