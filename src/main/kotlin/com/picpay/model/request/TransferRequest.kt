package com.picpay.model.request

import org.bson.types.ObjectId

data class TransferRequest (
    val originAccount: ObjectId,
    val destinyAccount: ObjectId,
    val amount: Double,
    val description: String?
)

