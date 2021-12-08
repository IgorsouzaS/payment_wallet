package com.picpay.model.response

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Transfer(
    @Id
    val id: ObjectId = ObjectId.get(),
    val originAccount: ObjectId,
    val destinyAccount: ObjectId,
    val description: String,

    val amount: Double,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val modifiedDate: LocalDateTime = LocalDateTime.now()
)
