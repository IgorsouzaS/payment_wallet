package com.picpay.repository

import com.picpay.model.response.Transfer
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TransferRepository : MongoRepository<Transfer, ObjectId>
