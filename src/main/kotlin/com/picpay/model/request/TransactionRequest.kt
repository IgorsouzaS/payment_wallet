package com.picpay.model.request

data class TransactionRequest (
    val originAccount: String,
    val destinyAccount: String,
    val destinyBankCode: String,
    val type: String,
    val amount: Double
)
