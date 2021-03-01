package io.codeal

import io.kotlintest.assertions.json.shouldMatchJson
import io.kotlintest.shouldBe
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test

class SimpleIntegrationTest {

    @Test
    fun `should save customer in the database`() {
        withTestApplication({ module() }) {
            val customerJson = /*language=JSON*/ """
                {
                  "id": "1",
                  "firstName": "Joe",
                  "lastName": "Doe",
                  "email": "joe.doe@thebackendengineer.io"
                } 
            """

            val saveCustomer = handleRequest(Post, "/customers") {
                addHeader("Content-Type", "application/json")
                setBody(customerJson)
            }

            val getCustomerResponse = handleRequest(Get, "/customers/1")
                .response
                .content ?: error("missing customer response")

            saveCustomer.response.status().shouldBe(Created)
            getCustomerResponse.shouldMatchJson(customerJson)
        }
    }
}