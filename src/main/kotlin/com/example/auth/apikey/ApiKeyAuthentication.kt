package com.example.auth.apikey

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class ApiKeyAuthentication(
    private val principal: String,
    private val hashedKey: String,
    private val authorities: List<String>,
    private var isAuthenticated: Boolean = false,
): Authentication {

    override fun getName(): String {
        return principal
    }

    override fun getAuthorities(): List<GrantedAuthority> {
        return authorities.map(::SimpleGrantedAuthority).toMutableList()
    }

    override fun getCredentials(): String {
        return hashedKey
    }

    override fun getPrincipal(): String {
        return hashedKey
    }

    override fun getDetails(): String {
        return "N/A"
    }

    override fun isAuthenticated(): Boolean {
        return isAuthenticated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }
}
