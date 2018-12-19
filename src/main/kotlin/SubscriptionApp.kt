@file:Suppress("MatchingDeclarationName")

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import service.DatabaseSubscriptionsService
import service.SubscriptionsService
import web.subscription

fun main() {
    embeddedServer(Netty, port = 8080, watchPaths = listOf("main"), module = Application::module).start()
}

fun Application.module() {
    val service: SubscriptionsService = DatabaseSubscriptionsService()
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
        subscription(service)
    }
}
