package com.example.controller

import com.example.common.ApplicationEndpoints.TEST_ENDPOINT
import com.example.common.ApplicationRoles.TEST_ROLE
import com.example.config.SECURITY_SCHEME_NAME
import com.example.config.ResourceOwner
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class TestController {

    @Operation(
        description = "Test operation",
        security = [ SecurityRequirement(name = SECURITY_SCHEME_NAME) ]
    )
    @GetMapping(TEST_ENDPOINT)
    @PostAuthorize("hasAuthority('$TEST_ROLE')")
    fun test(

        @Parameter(hidden = true)
        @ResourceOwner
        resourceOwner: String

    ): ResponseEntity<Map<String, String>> =
        ResponseEntity.ok(
            mapOf("resourceOwner" to resourceOwner)
        )
}
