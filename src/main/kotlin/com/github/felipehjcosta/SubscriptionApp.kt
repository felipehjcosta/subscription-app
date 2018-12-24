@file:Suppress("MatchingDeclarationName")

package com.github.felipehjcosta

import com.github.felipehjcosta.adapters.controllers.module
import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, watchPaths = listOf("main"), module = Application::module).start()
}
