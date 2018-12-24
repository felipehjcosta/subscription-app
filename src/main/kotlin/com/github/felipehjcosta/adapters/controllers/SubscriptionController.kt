package com.github.felipehjcosta.adapters.controllers

import com.github.felipehjcosta.application.ListSubscriptionService
import com.github.felipehjcosta.domain.SubscriptionsRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import com.github.felipehjcosta.model.Request

fun Routing.subscription(listSubscriptionService: ListSubscriptionService, repository: SubscriptionsRepository) {
    route("/subscription") {
        get("/") {
            call.respond(listSubscriptionService.execute().subscriptions)
        }
        post("/verify") {
            val request = call.receive<Request>()
            repository.findSubscriptions(request.userId, request.productId)?.let { subscription ->
                call.respond(subscription)
            } ?: call.respond(HttpStatusCode.NotFound, "Subscription Invalid")
        }
    }
}
