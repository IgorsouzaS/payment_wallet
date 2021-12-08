package com.picpay.model.response

import com.picpay.model.types.TransactionType
import java.time.LocalDateTime

abstract class Transaction {
    abstract val amount: Double
    abstract val type: TransactionType
    abstract val createdDate: LocalDateTime
    abstract val modifiedDate: LocalDateTime
}
