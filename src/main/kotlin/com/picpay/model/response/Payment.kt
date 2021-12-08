package com.picpay.model.response

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Payment(
    @Id
    val id: ObjectId = ObjectId.get(),
    val userId: ObjectId,
    val barcode: String,
    val amount: Double,
    val description: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val modifiedDate: LocalDateTime = LocalDateTime.now()
)
