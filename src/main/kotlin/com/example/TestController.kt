package com.example

import com.example.config.AuthSchemes.APIKEY
import com.example.config.AuthSchemes.OAUTH2
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping
class TestController {

    @Operation(
        description = "Test operation",
        security = [SecurityRequirement(name = OAUTH2), SecurityRequirement(name = APIKEY)]
    )
    @GetMapping("/test")
    @PostAuthorize("hasAuthority('USER')")
    fun test(

        @Parameter(hidden = true)
        principal: Principal,

    ): ResponseEntity<Map<String, String>> =
        ResponseEntity.ok(
            mapOf("resourceOwner" to principal.name)
        )
}
