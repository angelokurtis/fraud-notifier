package br.com.kurtis.fraud_notifier.domain.health

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.client.WebClient
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException
import java.net.ServerSocket
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
internal class HealthCheckVerticleTest {

    private var port: Int = 0
    private val log = LoggerFactory.getLogger(HealthCheckVerticleTest::class.java)

    @BeforeEach
    @Throws(IOException::class)
    internal fun deployVerticle(vertx: Vertx, context: VertxTestContext) {
        ServerSocket(0).use { this.port = it.localPort }
        log.info("Running tests on port ${this.port}")
        val options = DeploymentOptions().setConfig(JsonObject().put("HTTP_PORT", this.port))
        vertx.deployVerticle(HealthCheckVerticle(), options, context.succeeding { context.completeNow() })
    }

    @Test
    @DisplayName("Should start a Web Server")
    @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
    internal fun testHttpServer(vertx: Vertx, context: VertxTestContext) {
        WebClient.create(vertx)
                .get(this.port, "localhost", "/")
                .send {
                    assertThat(it.succeeded()).isTrue()
                    log.info("Server receive successfully the request")
                    val response = it.result()
                    assertThat(response.statusCode()).isEqualTo(200)
                    log.info("The response contains the expected body")
                    context.completeNow()
                }
    }
}