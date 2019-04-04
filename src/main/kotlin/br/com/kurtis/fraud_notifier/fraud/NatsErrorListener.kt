package br.com.kurtis.fraud_notifier.fraud

import io.nats.client.Connection
import io.nats.client.Consumer
import io.nats.client.ErrorListener
import io.vertx.core.logging.LoggerFactory

class NatsErrorListener : ErrorListener {

    private val log = LoggerFactory.getLogger(NatsErrorListener::class.java)

    override fun errorOccurred(connection: Connection, message: String) {
        log.error(message)
    }

    override fun exceptionOccurred(connection: Connection, e: Exception) {
        log.error("Exception occurred ", e)
    }

    override fun slowConsumerDetected(connection: Connection, consumer: Consumer) {
        log.info("Slow consumer detected $consumer")
    }
}
