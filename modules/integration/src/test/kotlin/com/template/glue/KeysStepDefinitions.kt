package com.template.glue

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.fail
import org.openapitools.client.apis.KeysApi
import org.openapitools.client.models.ApiKeyRequest
import org.openapitools.client.models.ApiKeyView

class KeysStepDefinitions(private val keysApi : KeysApi) {

    @When("creating api key with name {string}")
    fun whenCreatingApiKey(name: String) {
        findApiKey(name)?.also { fail("api key with name '$name' must not already exist") }
        keysApi.createApiKey(ApiKeyRequest(name = name, read = true, write = true, delete = true)).block()!!
    }

    @When("deleting api key with name {string}")
    fun whenDeletingApiKey(name: String) {
        val key = findApiKey(name) ?: fail("api key with name '$name' must exist")
        keysApi.deleteApiKey(key.id).block()
    }

    @Then("api key with name {string} does not exist")
    fun thenApiKeyNotExists(name: String) {
        findApiKey(name)?.also { fail("api key with name '$name' did exist: $it") }
    }

    @Then("api key with name {string} exists")
    fun thenApiKeyExists(name: String) {
        findApiKey(name) ?: fail("api key with name '$name' did not exist")
    }

    private fun findApiKey(name: String): ApiKeyView? =
        keysApi.getApiKeys().block()!!.firstOrNull { it.name == name }
}
