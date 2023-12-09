# Spring Kotlin Template

This project contains a complete template for a Kotlin Spring project, including:
- Multimodule setup with integration tests
- Spring security with OIDC and API key support
- Code formatting
- Proper exception handling and reporting
- OpenApi doc generation (code-first)

## Running locally without external dependencies

The first method is to run this application in the `local-mem` profile. In this profile, an in memory database is used
and a development API-Key is generated. Therefore, no external dependencies are required. Depending on the trajectory
of the project this profile might be removed, but it is useful for a quick start of the project.

## Running locally with cognito
The second method is to run this application in the `local` profile. In this profile, a real database and cognito
server are required. Then the application can be started from an IDE or by running the command blow, after inserting the
client secrets in the `application-local.yml` file:

```shell
./gradlew bootRun --args='--spring.profiles.active=local'
```
