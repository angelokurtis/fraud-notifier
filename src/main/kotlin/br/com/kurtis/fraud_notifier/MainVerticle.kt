package br.com.kurtis.fraud_notifier

import br.com.kurtis.fraud_notifier.domain.fraud.FraudVerticle
import br.com.kurtis.fraud_notifier.domain.health.HealthCheckVerticle
import br.com.kurtis.fraud_notifier.domain.mail.MailVerticle
import io.vertx.config.ConfigRetriever
import io.vertx.core.*
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory

class MainVerticle : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(MainVerticle::class.java)
    private val configs: JsonObject by lazy {
        val retrieveConfig = retrieveConfig()
        retrieveConfig.result()
    }
    private val verticles = arrayOf(
            FraudVerticle::class.java,
            HealthCheckVerticle::class.java,
            MailVerticle::class.java
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
        val options = DeploymentOptions()
        options.config = this.configs
        this.vertx.deployVerticle(name, options) {
            if (it.failed()) future.fail(it.cause())
            else future.complete()
        }
        return future
    }

    private fun retrieveConfig(): Future<JsonObject> {
        val future = Future.future<JsonObject>()
        val retriever = ConfigRetriever.create(this.vertx)
        retriever.getConfig {
            if (it.succeeded()) future.complete(it.result())
            else future.fail(it.cause())
        }
        return future
    }

}
