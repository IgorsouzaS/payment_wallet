package com.picpay.model.response

import com.picpay.model.types.TransactionType
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Deposit(
    @Id
    val id: ObjectId = ObjectId.get(),
    val destinyAccount: ObjectId,
    val userId: ObjectId,
    override val createdDate: LocalDateTime = LocalDateTime.now(),
    override val modifiedDate: LocalDateTime = LocalDateTime.now(),
    override val amount: Double,
    override val type: TransactionType
):Transaction()
