package com.example.config

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
class TestSecurityConfig
