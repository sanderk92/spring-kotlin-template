package com.template.glue

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.fail
import org.openapitools.client.apis.KeysApi
import org.openapitools.client.models.ApiKeyCreateCommand

class KeysStepDefinitions(private val keysApi : KeysApi) {

    @When("creating api key with name {string}")
    fun whenCreatingApiKey(name: String) {
        val command = ApiKeyCreateCommand(name = name, read = true, write = true, delete = true)
        keysApi.createApiKey(command).block()
    }

    @When("deleting api key with name {string}")
    fun whenDeletingApiKey(name: String) {
        val keys = keysApi.getApiKeys().block() ?: fail("could not retrieve api keys")
        val key = keys.firstOrNull { it.name == name } ?: fail("api key with name $name was not found")
        keysApi.deleteApiKey(key.id).block()
    }

    @Then("api key with name {string} exists")
    fun thenApiKeyExists(name: String) {
        val keys = keysApi.getApiKeys().block() ?: fail("could not retrieve api keys")
        val key = keys.firstOrNull { it.name == name }
        assertThat(key).isNotNull
    }

    @Then("api key with name {string} does not exist")
    fun thenApiKeyNotExists(name: String) {
        val keys = keysApi.getApiKeys().block() ?: fail("could not retrieve api keys")
        val key = keys.firstOrNull { it.name == name }
        assertThat(key).isNull()
    }
}