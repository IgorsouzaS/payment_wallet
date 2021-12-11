package com.picpay.controller

import com.picpay.config.Logger.logger
import com.picpay.exception.TransactionException
import com.picpay.model.request.TransactionRequest
import com.picpay.model.response.Transaction
import com.picpay.service.TransactionService
import io.swagger.annotations.ApiOperation
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/transactions")
class TransactionController(val transactionService: TransactionService) {

    @PostMapping("/")
    @ApiOperation(value = "Create transaction", response = Transaction::class)
    fun createTransaction(@RequestBody request: TransactionRequest) : ResponseEntity<Transaction> = try {

        val createdTransaction = transactionService.createTransaction(request).also {
            logger.info("Transaction created")
        }
        ResponseEntity(createdTransaction, HttpStatus.CREATED)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/{transactionId}")
    @ApiOperation(value = "Get transaction", response = Transaction::class)
    fun getTransaction(@PathVariable("transactionId") transactionId: String) : ResponseEntity<Transaction> = try {

        val transaction = transactionService.getTransaction(ObjectId(transactionId)).also {
            logger.info("Transaction with id $transactionId returned")
        }

        if(transaction.isPresent) {
            ResponseEntity.ok(transaction.get())
        }else{
            ResponseEntity.notFound().build()
        }
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }
}
