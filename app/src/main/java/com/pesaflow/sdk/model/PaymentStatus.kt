package com.pesaflow.sdk.model

enum class PaymentStatus {
    FORM_INPUT,      // User is filling out the form
    PROCESSING,      // Payment is being processed
    SUCCESS,         // Payment completed successfully
    FAILED           // Payment failed
}

data class PaymentError(
    val message: String,
    val reasons: List<String> = emptyList(),
    val suggestions: List<String> = emptyList()
)