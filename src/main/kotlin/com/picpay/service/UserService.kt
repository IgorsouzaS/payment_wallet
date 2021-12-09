package com.picpay.service

import com.picpay.config.Logger.logger
import com.picpay.exception.TransactionException
import com.picpay.model.response.Payment
import com.picpay.model.response.Transaction
import com.picpay.model.response.Transfer
import com.picpay.model.response.User
import com.picpay.model.types.TransactionType
import com.picpay.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val mongoTemplate: MongoTemplate // collection
) {

    fun createUser(user: User) : User{
        return userRepository.save(user).also {
            logger.info("Created user with id: ${it.id}")
        }
    }

    fun getUser(userId: ObjectId) : User{
        return findUser(userId)
    }

    fun getTransactions(userId: ObjectId, type: String) : List<Transaction> {
        findUser(userId)

        when(type){
            TransactionType.WITHDRAW.name, TransactionType.DEPOSIT.name, "" -> {
                val query = Query()
                var criteria = Criteria.where("userId").`is`(userId)

                if (type.isNotEmpty()){
                    criteria = criteria.andOperator(Criteria.where("type").`is`(type))
                }

                query.addCriteria(criteria)
                return mongoTemplate.find(query, Transaction::class.java, "")
            }
            else -> throw TransactionException("Transaction type is not valid")
        }
    }

    fun getTransfers(originAccountId: ObjectId): List<Transfer>{
        findUser(originAccountId)

        val query = Query()
        query.addCriteria(Criteria.where("originAccount").`is`(originAccountId))
        return mongoTemplate.find(query, Transfer::class.java)
    }

    fun getPayments(userId: ObjectId) : List<Payment>{
        findUser(userId)

        val query = Query()
        query.addCriteria(Criteria.where("userId").`is`(userId))
        return mongoTemplate.find(query, Payment::class.java)
    }

    private fun findUser(userId: ObjectId) : User{
        val user = userRepository.findById(userId)
        if (!user.isPresent){
            throw TransactionException("Unregistered user").also {
                logger.info("Unregistered user with id: $userId")
            }
        }
        return user.get()
    }
}
