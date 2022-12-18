package com.example.security.user

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Aspect
@Component
class CurrentUserExtractorAspect {

    @Pointcut("@annotation(com.example.security.user.CurrentUserExtractor)")
    fun hasCurrentUserExtractorAnnotation() {
    }

    @Pointcut("args(currentUser, ..)")
    fun hasCurrentUserAsFirstArg(currentUser: CurrentUser) {
    }

    @Before("hasCurrentUserExtractorAnnotation() && hasCurrentUserAsFirstArg(currentUser)")
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
        val authorities = authentication.authorities.map(GrantedAuthority::toString)
        val authorityField = currentUser.javaClass.getDeclaredField(CurrentUser::authorities.name)
        authorityField.isAccessible = true
        authorityField.set(currentUser, authorities)
        authorityField.isAccessible = false
    }
}
