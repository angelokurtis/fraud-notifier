package br.com.kurtis.fraud_notifier.fraud.message

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(var type: String? = null,
                   var orderId: String? = null,
                   var customerId: String? = null,
                   var data: Data? = null)
