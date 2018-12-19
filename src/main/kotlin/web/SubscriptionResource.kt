package web

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import model.Request
import model.Response
import service.SubscriptionsApi

fun Routing.subscription(subscriptionsApi: SubscriptionsApi) {
    post("/verify") {
        val request = call.receive<Request>()
        val status = subscriptionsApi.findSubscriptions(request.userId, request.productId)?.let { "OK" } ?: "FAILURE"
        call.respond(Response(status))
    }
}
