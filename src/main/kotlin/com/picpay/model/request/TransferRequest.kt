package com.picpay.model.request

data class TransferRequest (
    val originAccount: String,
    val destinyAccount: String,
    val amount: Double,
    val description: String?
)

