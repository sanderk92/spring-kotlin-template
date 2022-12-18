# Spring Security Keycloak

A simple Spring Security implementation for Keycloak, including:

- API Key management
- Swagger with OIDC and API-Key flow
- A generic Spring adapter, non-Keycloak specific
- WebMvc controller tests
- Integration test security configuration

### Keycloak config
In order to start a development Keycloak instance, run:

```shell 
docker compose up
```
1. Go to `http://localhost:8081/auth` 
   - login with `admin`/`password` 
2. Create a public client `public`
   - set `Valid Redirect URIs` to `*`
   - set `Web Origins` to `*`
3. Create a user

You can now follow the login flows on `http://localhost:8080/swagger-ui/index.html#/` using your user credentials or
api keys generated for that user.
