package br.com.kurtis.fraud_notifier.infra.error

class ConfigNotFoundException(propertyName: String) : RuntimeException("The property $propertyName was not found.")
