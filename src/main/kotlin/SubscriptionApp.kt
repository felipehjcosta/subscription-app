@file:Suppress("MatchingDeclarationName")

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
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
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import service.DatabaseSubscriptionsApi
import service.SubscriptionsApi

fun main() {
    embeddedServer(Netty, port = 8080, watchPaths = listOf("SubscriptionAppkt"), module = Application::module).start()
}

fun Application.module() {
    val api: SubscriptionsApi = DatabaseSubscriptionsApi()
    install(StatusPages) {
        exception<Throwable> {
            call.respondText(it.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }
    install(ContentNegotiation) {
        gson()
    }
    routing {
        get("/") {
            call.respondText("Hello World", ContentType.Text.Html)
        }
        post("/verify") {
            val request = call.receive<Request>()
            val status = api.findSubscriptions(request.userId, request.productId)?.let { "OK" } ?: "FAILURE"
            call.respond(Response(status))
        }
    }
}

data class Request(val userId: String, val productId: String)

data class Response(val status: String)
