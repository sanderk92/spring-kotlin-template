package com.example.security.apikey

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class ApiKeyAuthentication(
    private val name: String,
    private val hashedKey: String,
    private val authorities: List<String>,
    private var isAuthenticated: Boolean = true,
) : Authentication {

    override fun getName(): String {
        return name
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
