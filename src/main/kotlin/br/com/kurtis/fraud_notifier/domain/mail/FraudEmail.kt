package br.com.kurtis.fraud_notifier.domain.mail

import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class FraudEmail(
        private val serverAddress: String,
        private val orderId: String,
        private val destination: String,
        private val invalidateFraudLink: String) {

    fun send() {
        val prop = Properties()
        prop["mail.smtp.host"] = serverAddress
        prop["mail.smtp.port"] = "1025"
        val message = MimeMessage(Session.getDefaultInstance(prop))
        message.setFrom(InternetAddress("dont_reply@fraud-system.com"))
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.destination))
        message.subject = "Fraud Detection"

        val text = """
                Hi,<br/><br/>
                A fraud was detected in one of your transactions.<br/>
                <b>If you are sure</b> that this transaction was made by you then click <a href="http://$invalidateFraudLink/$orderId">here</a>.
                <br/><br/>
                Thanks,<br/>
                Fraud Platform/API Trust & Safety
            """
        val mimeBodyPart = MimeBodyPart()
        mimeBodyPart.setContent(text, "text/html")

        val multipart = MimeMultipart()
        multipart.addBodyPart(mimeBodyPart)

        message.setContent(multipart)

        Transport.send(message)
    }
}
