package com.picpay.exception

data class TransactionException(
    override val message: String
): Exception()