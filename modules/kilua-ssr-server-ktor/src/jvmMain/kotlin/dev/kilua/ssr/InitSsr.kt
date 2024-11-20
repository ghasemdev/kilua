/*
 * Copyright (c) 2024 Robert Jaros
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.kilua.ssr

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import java.util.*

internal val ssrEngineKey: AttributeKey<SsrEngine> = AttributeKey("ssrEngine")

/**
 * Initialization function for Kilua Server-Side Rendering.
 */
public fun Application.initSsr() {
    val nodeExecutable = environment.config.propertyOrNull("ssr.nodeExecutable")?.getString()
    val port = environment.config.propertyOrNull("ssr.port")?.getString()?.toIntOrNull()
    val externalSsrService = environment.config.propertyOrNull("ssr.externalSsrService")?.getString()
    val rpcUrlPrefix = environment.config.propertyOrNull("ssr.rpcUrlPrefix")?.getString()
    val rootId = environment.config.propertyOrNull("ssr.rootId")?.getString()
    val contextPath = environment.config.propertyOrNull("ssr.contextPath")?.getString()
    val noCache = environment.config.propertyOrNull("ssr.noCache")?.getString()?.toBooleanStrictOrNull() ?: false
    val ssrEngine = SsrEngine(nodeExecutable, port, externalSsrService, rpcUrlPrefix, rootId, contextPath, noCache)
    attributes.put(ssrEngineKey, ssrEngine)
    routing {
        get("/index.html") {
            respondSsr()
        }
        singlePageApplication {
            defaultPage = UUID.randomUUID().toString() // Non-existing resource
            filesPath = "/assets"
            useResources = true
        }
        route("/") {
            route("{static-content-path-parameter...}") {// Important name from Ktor sources!
                get {
                    respondSsr()
                }
            }
        }
    }
}

private suspend fun RoutingContext.respondSsr() {
    if (call.request.path() == "/favicon.ico") {
        call.respond(HttpStatusCode.NotFound)
    } else {
        val ssrEngine = call.application.attributes[ssrEngineKey]
        call.respondText(ContentType.Text.Html, HttpStatusCode.OK) {
            val language = call.request.acceptLanguageItems().firstOrNull()?.value
            ssrEngine.getSsrContent(call.request.uri, language)
        }
    }
}
