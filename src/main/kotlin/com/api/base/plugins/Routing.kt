package com.api.base.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/token") {
            call.respondText("abc123")
        }

        authenticate {
            get("/protected") {
                call.respondText("Accessing protected API")
            }
        }
    }
}
