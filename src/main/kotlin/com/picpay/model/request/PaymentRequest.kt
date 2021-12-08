package com.picpay.model.request

data class PaymentRequest (
    val userId: String,
    val barcode: String,
    val amount: Double,
    val description: String? = null
)
