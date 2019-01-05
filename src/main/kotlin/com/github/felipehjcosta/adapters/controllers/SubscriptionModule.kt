package com.github.felipehjcosta.adapters.controllers

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText

fun Application.module() {
    install(StatusPages) {
        exception<Throwable> {
            call.respondText(it.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }
    install(CallLogging)
    install(ContentNegotiation) {
        gson()
    }
}
