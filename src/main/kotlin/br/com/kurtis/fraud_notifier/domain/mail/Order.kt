package br.com.kurtis.fraud_notifier.domain.mail

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Order(var type: String? = null,
                 var orderId: String? = null,
                 var customerId: String? = null,
                 var data: Data? = null)