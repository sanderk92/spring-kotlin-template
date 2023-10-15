package com.template.config.security.apikey

import com.nimbusds.jose.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import org.springframework.stereotype.Service

internal interface HashGenerator {
    fun hash(string: String): String
}

@Service
internal class Sha256HashGenerator : HashGenerator {

    override fun hash(string: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(string.toByteArray(StandardCharsets.UTF_8))
        return Base64.encode(hash).toString()
    }
}
