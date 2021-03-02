package io.codeal

import org.jdbi.v3.core.Jdbi

data class Customer(val id: String, val firstName: String, val lastName: String, val email: String)

val customerRepository = CustomerRepository(
    Jdbi.create(
        "jdbc:postgresql://localhost:${System.getProperty("postgres.tcp.5432")}/codeal",
        "codeal",
        "password"
    )
)

class CustomerRepository(private val jdbi: Jdbi) {

    fun findById(id: String): Customer? = jdbi.withHandle<Customer, Exception> {
        it.createQuery("select * from customer where id = :id")
            .bind("id", id)
            .map { rs, ctx ->
                Customer(
                    id = rs.getString("id"),
                    firstName = rs.getString("first_name"),
                    lastName = rs.getString("last_name"),
                    email = rs.getString("email")
                )
            }.findOne().orElse(null)
    }

    fun save(customer: Customer) {
        jdbi.useHandle<Exception> {
            it.createUpdate(
                "INSERT INTO customer(id, first_name, last_name, email) " +
                        "VALUES (:id, :first_name, :last_name, :email)"
            )
                .bind("id", customer.id)
                .bind("first_name", customer.firstName)
                .bind("last_name", customer.lastName)
                .bind("email", customer.email)
                .execute()
        }
    }
}