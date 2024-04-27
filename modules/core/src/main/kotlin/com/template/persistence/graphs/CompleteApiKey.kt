package com.template.persistence.graphs

import org.springframework.data.jpa.repository.EntityGraph

@EntityGraph(
    attributePaths = [
        "authorities",
        "owner",
    ],
)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CompleteApiKey
