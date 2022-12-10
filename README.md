# Spring Security Keycloak

A simple Spring Security implementation for Keycloak, including:

- API Key management
- Swagger with OAUTH2 and API-Key flow
- A generic Spring adapter, non-Keycloak specific
- WebMvc controller tests with JWT mocks
- Integration tests with JWT mocks

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

You can now follow the login flow on `http://localhost:8080/swagger-ui/index.html#/` using credentials `user`/`password`

### Usage example
With this project we can easily secure our endpoints and extract the Principal of the authenticated user in our 
controllers.

```kotlin
@RestController
@RequestMapping
class TestController {

   @Operation(
      description = "Test operation",
      security = [ SecurityRequirement(name = OAUTH2), SecurityRequirement(name = APIKEY) ]
   )
   @GetMapping("/test")
   @PostAuthorize("hasRole('ROLE_USER') or hasAuthority('AUTHORITY_READ')")
   fun test(

      @Parameter(hidden = true)
      principal: Principal,

      ): ResponseEntity<Map<String, String>> =
      ResponseEntity.ok(
         mapOf("resourceOwner" to principal.name)
      )
}

```