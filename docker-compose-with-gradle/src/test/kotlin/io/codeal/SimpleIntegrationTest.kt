package io.codeal

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class SimpleIntegrationTest {

    @Test
    fun `should save customer in the database`() {
        val savedCustomer = Customer(
            id = UUID.randomUUID().toString(),
            firstName = "Joe",
            lastName = "Doe",
            email = "joe.doe@email.com"
        )
        customerRepository.save(savedCustomer)

        val customer = customerRepository.findById(savedCustomer.id)
        assertEquals(customer, savedCustomer)
    }
}