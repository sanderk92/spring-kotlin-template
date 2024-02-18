package com.template.glue

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.fail
import org.openapitools.client.apis.ApiKeysApi
import org.openapitools.client.models.ApiKeyDto
import org.openapitools.client.models.ApiKeyRequest

internal class ApiKeysStepDefinitions(private val apiKeysApi : ApiKeysApi) {

    @When("creating api key with name {string} and all authorities")
    fun whenCreatingApiKey(name: String) {
        findApiKey(name)?.also { fail("api key with name '$name' must not already exist") }
        apiKeysApi.createApiKey(ApiKeyRequest(name = name, read = true, write = true, delete = true))
    }

    @Then("api key with name {string} exists with all authorities")
    fun thenApiKeyExists(name: String) {
        val key = findApiKey(name) ?: fail("api key with name '$name' did not exist")
        assertThat(key.authorities).containsExactly("READ", "WRITE", "DELETE")
    }

    @When("deleting api key with name {string}")
    fun whenDeletingApiKey(name: String) {
        val key = findApiKey(name) ?: fail("api key with name '$name' must exist")
        apiKeysApi.deleteApiKey(key.id)
    }

    @Then("api key with name {string} does not exist")
    fun thenApiKeyNotExists(name: String) {
        findApiKey(name)?.also { fail("api key with name '$name' did exist: $it") }
    }

    private fun findApiKey(name: String): ApiKeyDto? =
        apiKeysApi.getApiKeys().firstOrNull { it.name == name }
}
