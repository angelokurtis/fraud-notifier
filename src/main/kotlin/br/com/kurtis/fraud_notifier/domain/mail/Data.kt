package br.com.kurtis.fraud_notifier.domain.mail

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Data(var fraudId: String? = null,
                var email: String? = null,
                var reason: String? = null)
