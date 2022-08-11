# Spring Security Keycloak

A simple Spring Security implementation for Keycloak JWT tokens including:

- A generic Spring adapter, non-Keycloak specific
- WebMvc controller tests with JWT mocks
- Integration tests with JWT mocks
- Swagger page with OAUTH2 flow
- Keycloak setup instructions

### Keycloak config
In order to start a development Keycloak instance, run:

```shell 
docker compose up
```

<sub>*A user `user`/`password` is inserted as specified in the docker compose file.

1. Create a public client `public`
   - set `Valid Redirect URIs` to `*`
   - set `Web Origins` to `*`
2. Create a role `test`
3. Assign role `test` to `user`

You can now follow the login flow on `http://localhost:8080/swagger-ui/index.html#/` using credentials `user`/`password`

### Usage example
With this project we can easily secure our endpoints and extract the `sub` of the JWT token, all while Swagger is
facilitating the PKCE flow for us:

```kotlin
 @Operation(
     description = "Test operation",
     security = [ SecurityRequirement(name = SECURITY_SCHEME_NAME) ]
 )
 @GetMapping("/test-path")
 @PostAuthorize("hasAuthority('test_role')")
 fun test(

     @Parameter(hidden = true)
     @ResourceOwner
     resourceOwner: String

 ) = ResponseEntity.ok().build<Void>()
```