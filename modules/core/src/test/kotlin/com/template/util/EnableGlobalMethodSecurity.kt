package com.template.util

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, proxyTargetClass = true)
internal class EnableGlobalMethodSecurity
