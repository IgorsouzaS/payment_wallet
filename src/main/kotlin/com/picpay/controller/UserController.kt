package com.picpay.controller

import com.picpay.exception.TransactionException
import com.picpay.model.response.Payment
import com.picpay.model.response.Transaction
import com.picpay.model.response.Transfer
import com.picpay.common.Logger.logger
import com.picpay.service.UserService
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/users")
class UserController(private val userService: UserService){

    @GetMapping("/{userId}/payments")
    fun getUserPayments(@PathVariable("userId", required = true) userId: ObjectId)
    : ResponseEntity<List<Payment>> = try {
        val payments = userService.getPayments(userId).onEach {
            logger.info("Payment with id ${it.id} returned")
        }
        ResponseEntity.ok(payments)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/{userId}/transactions")
    fun getUserTransactions(@PathVariable("userId", required = true) userId: ObjectId,
                            @RequestParam type: String?) : ResponseEntity<List<Transaction>> = try {
        val deposits = userService.getTransactions(userId, type.orEmpty()).onEach {
            logger.info("Transaction $it returned")
        }
        ResponseEntity.ok(deposits)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/{userId}/transfers")
    fun getUserTransfers(@PathVariable("userId", required = true) userId: ObjectId)
    : ResponseEntity<List<Transfer>> = try {
        val transfers = userService.getTransfers(userId).onEach {
            logger.info("Transaction with id ${it.id} returned")
        }
        ResponseEntity.ok(transfers)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }
}