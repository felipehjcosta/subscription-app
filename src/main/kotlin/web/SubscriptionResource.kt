package web

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import model.Request
import model.Response
import service.SubscriptionsService

fun Routing.subscription(service: SubscriptionsService) {
    post("/verify") {
        val request = call.receive<Request>()
        val status = service.findSubscriptions(request.userId, request.productId)?.let { "OK" } ?: "FAILURE"
        call.respond(Response(status))
    }
}
