package br.com.kurtis.fraud_notifier.health

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.logging.LoggerFactory

class HealthCheckVerticle : AbstractVerticle() {

    companion object {
        private const val DEFAULT_PORT = 8888
    }

    private val log = LoggerFactory.getLogger(HealthCheckVerticle::class.java)

    override fun start(startFuture: Future<Void>) {
        this.vertx
                .createHttpServer()
                .requestHandler {
                    it.response()
                            .putHeader("content-type", "text/plain")
                            .end("FraudNotifier is up and running!")
                }
                .listen(this.config().getInteger("http.port") ?: DEFAULT_PORT) {
                    if (it.succeeded()) {
                        startFuture.complete()
                        this.log.info("HTTP server started on port ${it.result().actualPort()}")
                    } else startFuture.fail(it.cause())
                }
    }
}
