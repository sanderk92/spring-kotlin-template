package com.template.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
class AopConfig
