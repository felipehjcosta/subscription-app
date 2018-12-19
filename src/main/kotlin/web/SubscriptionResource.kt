package web

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import model.Request
import service.SubscriptionsService

fun Routing.subscription(service: SubscriptionsService) {
    route("/subscription") {
        get("/") {
            call.respond(service.all())
        }
        post("/verify") {
            val request = call.receive<Request>()
            service.findSubscriptions(request.userId, request.productId)?.let { subscription ->
                call.respond(subscription)
            } ?: call.respond(HttpStatusCode.NotFound, "Subscription Invalid")
        }
    }
}
