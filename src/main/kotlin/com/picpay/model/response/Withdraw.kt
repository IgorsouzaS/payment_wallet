package com.picpay.model.response

import com.picpay.model.types.TransactionType
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Withdraw(
    @Id
    val id: ObjectId = ObjectId.get(),
    val userId: ObjectId,
    val originAccount: ObjectId,
    val destinyAccount: String,
    val destinyBankCode: String? = null,
    override val amount: Double,
    override val createdDate: LocalDateTime = LocalDateTime.now(),
    override val modifiedDate: LocalDateTime = LocalDateTime.now(),
    override val type: TransactionType
): Transaction()
