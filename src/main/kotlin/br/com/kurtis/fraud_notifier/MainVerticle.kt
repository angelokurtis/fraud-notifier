package br.com.kurtis.fraud_notifier

import br.com.kurtis.fraud_notifier.fraud.FraudVerticle
import br.com.kurtis.fraud_notifier.health.HealthCheckVerticle
import io.vertx.core.*
import io.vertx.core.logging.LoggerFactory

class MainVerticle : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(MainVerticle::class.java)
    private val verticles = arrayOf(
            FraudVerticle::class.java,
            HealthCheckVerticle::class.java
    )

    override fun init(vertx: Vertx, context: Context) {
        super.init(vertx, context)
        this.log.info("MainVerticle initialized!")
    }

    override fun start(startFuture: Future<Void>) {
        verticles
                .map { it.name }
                .map { deployVerticle(it) }
                .let { CompositeFuture.all(it) }
                .setHandler {
                    if (it.succeeded()) startFuture.complete()
                    else startFuture.fail(it.cause())
                }
    }

    private fun deployVerticle(name: String): Future<Void> {
        val future = Future.future<Void>()
        this.vertx.deployVerticle(name) {
            if (it.failed()) future.fail(it.cause())
            else future.complete()
        }
        return future
    }
}
