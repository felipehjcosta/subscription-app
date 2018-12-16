import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, watchPaths = listOf(""), module = Application::module).start()
}

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Hello World", ContentType.Text.Html)
        }
        post("/verify") {
            call.respond("OK")
        }
    }
}