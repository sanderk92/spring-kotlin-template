package com.template.config.security.user

import java.util.UUID
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Aspect
@Component
internal class CurrentUserAspect {
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    fun isRestController() {
    }

    @Pointcut("args(currentUser, ..)")
    fun hasFirstArg(currentUser: CurrentUser) {
    }

    @Before("isRestController() && hasFirstArg(currentUser)")
    fun setUserId(currentUser: CurrentUser) {
        val authentication = SecurityContextHolder.getContext().authentication
        setIdField(authentication, currentUser)
        setAuthoritiesField(authentication, currentUser)
    }

    private fun setIdField(authentication: Authentication, currentUser: CurrentUser) {
        val uuid = UUID.fromString(authentication.name)
        val idField = currentUser.javaClass.getDeclaredField(CurrentUser::id.name)
        idField.isAccessible = true
        idField.set(currentUser, uuid)
        idField.isAccessible = false
    }

    private fun setAuthoritiesField(authentication: Authentication, currentUser: CurrentUser) {
        val authorities = authentication.authorities.map(GrantedAuthority::toString).map(Authority.Companion::valueOfRole)
        val authorityField = currentUser.javaClass.getDeclaredField(CurrentUser::authorities.name)
        authorityField.isAccessible = true
        authorityField.set(currentUser, authorities)
        authorityField.isAccessible = false
    }
}
