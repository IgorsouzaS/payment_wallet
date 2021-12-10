package com.picpay.controller

import com.picpay.config.Logger.logger
import com.picpay.exception.TransactionException
import com.picpay.model.request.UserRequest
import com.picpay.model.response.Payment
import com.picpay.model.response.Transaction
import com.picpay.model.response.Transfer
import com.picpay.model.response.User
import com.picpay.service.UserService
import io.swagger.annotations.ApiOperation
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/users")
class UserController(private val userService: UserService){

    @PostMapping("/")
    fun createUser(@RequestBody request: UserRequest) : ResponseEntity<User> = try {
        val user = userService.createUser(request).also {
            logger.info("User $it returned")
        }
        ResponseEntity.ok(user)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable("userId", required = true) userId: String)
    : ResponseEntity<User> = try {
        val user = userService.getUser(ObjectId(userId)).also {
            logger.info("User $it returned")
        }
        ResponseEntity.ok(user)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/{userId}/payments")
    @ApiOperation(value = "Create transfer", response = Payment::class, responseContainer = "List")
    fun getUserPayments(@PathVariable("userId", required = true) userId: String)
    : ResponseEntity<List<Payment>> = try {
        val payments = userService.getPayments(ObjectId(userId)).onEach {
            logger.info("Payment with id ${it.id} returned")
        }
        ResponseEntity.ok(payments)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/{userId}/transactions")
    @ApiOperation(value = "Create transfer", response = Transaction::class, responseContainer = "List")
    fun getUserTransactions(@PathVariable("userId", required = true) userId: String,
                            @RequestParam type: String?) : ResponseEntity<List<Transaction>> = try {
        val deposits = userService.getTransactions(ObjectId(userId), type.orEmpty()).onEach {
            logger.info("Transaction $it returned")
        }
        ResponseEntity.ok(deposits)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/{userId}/transfers")
    @ApiOperation(value = "Create transfer", response = Transfer::class, responseContainer = "List")
    fun getUserTransfers(@PathVariable("userId", required = true) userId: String)
    : ResponseEntity<List<Transfer>> = try {
        val transfers = userService.getTransfers(ObjectId(userId)).onEach {
            logger.info("Transaction with id ${it.id} returned")
        }
        ResponseEntity.ok(transfers)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }
}
