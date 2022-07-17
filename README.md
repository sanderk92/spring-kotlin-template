# Spring Security Keycloak

A simple Spring Security implementation for Keycloak JWT tokens including:

- Controller tests with JWT mock
- Swagger page with OAUTH2 flow
- Docker compose for Keycloak server
- A generic Spring adapter, non-Keycloak specific

### Keycloak config
For this example to work, make sure to have configured the following Keycloak server:

- Create a public client `public`
  - Valid Redirect URIs (`*` for dev)
  - Web Origins (`*` for dev)
- Create a role `test`
- Assign role `test` to `user`

You can now follow the login flow on `http://localhost:8080/swagger-ui/index.html#/` using credentials `user`/`password`




