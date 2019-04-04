package br.com.kurtis.fraud_notifier

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.logging.LoggerFactory

class MainVerticle : AbstractVerticle() {

    companion object {
        private const val DEFAULT_PORT = 8888
    }

    private val log = LoggerFactory.getLogger(MainVerticle::class.java)

    override fun start(startFuture: Future<Void>) {
        vertx
                .createHttpServer()
                .requestHandler {
                    it.response()
                            .putHeader("content-type", "text/plain")
                            .end("Hello from Vert.x!")
                }
                .listen(this.config().getInteger("http.port") ?: DEFAULT_PORT) {
                    if (it.succeeded()) {
                        startFuture.complete()
                        log.info("HTTP server started on port ${it.result().actualPort()}")
                    } else {
                        startFuture.fail(it.cause())
                    }
                }

    }

}
