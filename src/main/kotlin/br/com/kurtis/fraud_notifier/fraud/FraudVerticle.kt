package br.com.kurtis.fraud_notifier.fraud

import io.nats.client.Connection
import io.nats.client.Message
import io.nats.client.Nats
import io.nats.client.Options
import io.vertx.core.AbstractVerticle
import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import java.nio.charset.StandardCharsets

class FraudVerticle : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(FraudVerticle::class.java)
    private var connection: Connection? = null

    override fun init(vertx: Vertx, context: Context) {
        super.init(vertx, context)
        log.info("FraudVerticle initialized!")
    }

    override fun start() {
        val server = this.config().getString("nats.server") ?: "localhost"
        val options = Options.Builder()
                .server("nats://$server:4222")
                .connectionListener(NatsConnectionListener())
                .errorListener(NatsErrorListener())
                .build()
        connection = Nats.connect(options)
        connection!!.createDispatcher { message -> log(message) }.subscribe("request-claims")
    }

    override fun stop() {
        super.stop()
        connection?.close()
        log.info("FraudVerticle stopped!")
    }

    private fun log(message: Message) {
        val response = String(message.data, StandardCharsets.UTF_8)
        log.info(response)
    }
}