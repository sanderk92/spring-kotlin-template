package com.template.glue

import io.cucumber.java.en.Then

class StepDefinitions {

    @Then("test environment works")
    fun testEnvironmentWorks() {
        println("Test environment works!")
    }
}
