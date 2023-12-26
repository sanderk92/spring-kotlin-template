package com.template.config

import java.util.UUID
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

typealias IdGenerator<T> = () -> T

@Configuration
internal class IdGeneratorBeans {
    @Bean
    fun idGenerator(): IdGenerator<UUID> = { UUID.randomUUID() }
}
