package com.api.base.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.defaultheaders.*

fun Application.configureDefaultHeader() {
    install(DefaultHeaders) {
    }
}