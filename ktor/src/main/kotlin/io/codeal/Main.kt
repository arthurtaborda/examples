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
import kotlinx.serialization.Serializable

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        customerRouting()
    }
}

@Serializable
data class Customer(val id: String, val firstName: String, val lastName: String, val email: String)

val customers = mutableListOf<Customer>()

fun Route.customerRouting() {
    route("/customers") {
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val customer = customers.firstOrNull { it.id == id } ?: return@get call.respondText(
                "No customer with id $id",
                status = HttpStatusCode.NotFound
            )

            call.respond(customer)
        }
        post {
            val customer = call.receive<Customer>()
            customers.add(customer)
            call.respond(Created)
        }
    }
}