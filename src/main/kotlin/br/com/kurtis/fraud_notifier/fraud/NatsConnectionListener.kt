package br.com.kurtis.fraud_notifier.fraud

import io.nats.client.Connection
import io.nats.client.ConnectionListener
import io.nats.client.ConnectionListener.Events
import io.vertx.core.logging.LoggerFactory

class NatsConnectionListener : ConnectionListener {

    private val log = LoggerFactory.getLogger(NatsConnectionListener::class.java)

    override fun connectionEvent(connection: Connection, events: Events) {
        log.info("Connection event: $events")
    }
}
