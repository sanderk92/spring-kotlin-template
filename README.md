# Kotlin Spring Template
A template application for a rest api in Kotlin with Spring

## Running locally
To run this application for development, run it in the `local` profile. In this profile, everything is stored locally
and an AWS Cognito authentication server is used. The cognito authentication server is pre-configured. The application
can be started from an IDE or by running the command blow.

```shell
./gradlew modules:core:bootRun --args='--spring.profiles.active=local'
```

### Test data
When running with the `local` profile, test data is generated at startup. This test data is generated for a random user,
of which data can always be accessed through the generated api key. If you want this test data to be generated for your
real user, override the FEATURE_DEV_GENERATED_USER_ID environment variable to contain the `sub` claim from the JWT token
provided by AWS Cognito. The generated data will then be visible when logging in with your account.

## Rebuilding OpenApi spec and clients for integration testing
In order to trigger the regeneration of a new openapi spec and successively regenerate the clients in the integration
module, i.e. after an update to the API interface, make sure to run `./gradlew clean` on the core module first.

```shell
./gradlew modules:core:clean
```
```shell
./gradlew modules:integration:openApiGenerate
```
```shell
./gradlew modules:integration:test
```
