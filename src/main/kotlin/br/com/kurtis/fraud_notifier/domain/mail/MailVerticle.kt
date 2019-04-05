package br.com.kurtis.fraud_notifier.domain.mail

import br.com.kurtis.fraud_notifier.infra.error.ConfigNotFoundException
import io.vertx.core.AbstractVerticle
import io.vertx.core.Context
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory

class MailVerticle : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(MailVerticle::class.java)
    private val serverAddress by lazy {
        this.config().getString("MAIL_SERVER_ADDRESS") ?: throw ConfigNotFoundException("MAIL_SERVER_ADDRESS")
    }

    override fun init(vertx: Vertx?, context: Context?) {
        super.init(vertx, context)
        this.log.info("MailVerticle initialized!")
    }

    override fun start(startFuture: Future<Void>?) {
        val eventBus = this.vertx.eventBus()
        eventBus.consumer<JsonObject>(FraudNotificationTopic.ADDRESS) { message ->
            val (_, orderId, _, data) = (message.body()
                    ?.let { it.mapTo(Order::class.java) }
                    ?: throw UnexpectedMessageException())
            val (_, email, _) = data!!
            val invalidateFraudLink = this.config().getString("INVALIDATE_FRAUD_LINK") ?: ""
            FraudEmail(this.serverAddress, orderId!!, email!!, invalidateFraudLink).send()
            this.log.info("The notification was sent to $email")
        }
    }
}

class UnexpectedMessageException : RuntimeException("A message was received in unknown format.")