package io.codeal

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.netty.EngineMain
import io.codeal.repository.CustomerRepository
import org.jdbi.v3.core.Jdbi

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        customerRouting()
    }
}

val customerRepository = CustomerRepository(
    Jdbi.create(
        "jdbc:postgresql://localhost:${System.getProperty("postgres.port")}/thebackendengineer", // url
        "thebackendengineer", // username
        "12345"
    )
)

fun Route.customerRouting() {
    route("/customers") {
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val customer = customerRepository.findById(id) ?: return@get call.respondText(
                "No customer with id $id",
                status = HttpStatusCode.NotFound
            )

            call.respond(customer)
        }
        post {
            val customer = call.receive<Customer>()
            customerRepository.save(customer)
            call.respond(Created)
        }
    }
}