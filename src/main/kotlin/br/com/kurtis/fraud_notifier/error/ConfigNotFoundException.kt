package br.com.kurtis.fraud_notifier.error

class ConfigNotFoundException(propertyName: String) : RuntimeException("The property $propertyName was not found.")
