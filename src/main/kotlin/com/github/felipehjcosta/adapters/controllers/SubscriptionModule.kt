package com.github.felipehjcosta.adapters.controllers

import com.github.felipehjcosta.adapters.infrastructure.DatabaseSubscriptionsRepository
import com.github.felipehjcosta.application.ListSubscriptionService
import com.github.felipehjcosta.application.VerifySubscriptionService
import com.github.felipehjcosta.application.VerifySubscriptionServiceRequest
import com.github.felipehjcosta.application.VerifySubscriptionServiceResponse
import com.github.felipehjcosta.model.Request
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing

fun Application.module() {
    val repository = DatabaseSubscriptionsRepository()
    val listSubscriptionService = ListSubscriptionService(repository)
    val verifySubscriptionService = VerifySubscriptionService(repository)
    moduleWithDependencies(verifySubscriptionService, listSubscriptionService)
}

fun Application.moduleWithDependencies(
    verifySubscriptionService: VerifySubscriptionService,
    listSubscriptionService: ListSubscriptionService
) {
    install(StatusPages) {
        exception<Throwable> {
            call.respondText(it.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }
    install(CallLogging)
    install(ContentNegotiation) {
        gson()
    }
    routing {
        get("/") {
            call.respondText("Hello World", ContentType.Text.Html)
        }
        route("/subscriptions") {
            get("/") {
                call.respond(listSubscriptionService.execute().subscriptions)
            }
            post("/verify") {
                val request = call.receive<Request>()
                val verifyRequest = VerifySubscriptionServiceRequest(request.userId, request.productId)
                val response = verifySubscriptionService.execute(verifyRequest)
                when (response) {
                    is VerifySubscriptionServiceResponse.VerifySubscriptionServiceExists -> {
                        call.respond(HttpStatusCode.OK)
                    }
                    is VerifySubscriptionServiceResponse.VerifySubscriptionServiceNotExists -> {
                        call.respond(HttpStatusCode.NotFound, "Invalid Subscription")
                    }
                }
            }
        }
    }
}
