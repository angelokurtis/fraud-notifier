package br.com.kurtis.fraud_notifier.mail

import io.vertx.core.AbstractVerticle
import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory

class MailVerticle : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(MailVerticle::class.java)

    override fun init(vertx: Vertx?, context: Context?) {
        super.init(vertx, context)
        log.info("MailVerticle initialized!")
    }
}

