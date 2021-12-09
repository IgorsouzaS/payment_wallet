package com.picpay.controller

import com.picpay.config.Logger.logger
import com.picpay.exception.TransactionException
import com.picpay.model.request.TransactionRequest
import com.picpay.model.response.Deposit
import com.picpay.model.response.Transaction
import com.picpay.model.response.Withdraw
import com.picpay.model.types.TransactionType
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
        val createdTransaction : Transaction
        when(request.type) {
            TransactionType.DEPOSIT.name -> {
                val deposit = Deposit(
                    amount = request.amount,
                    destinyAccount = ObjectId(request.destinyAccount),
                    userId = ObjectId(request.destinyAccount),
                    type = TransactionType.DEPOSIT
                )
                createdTransaction = transactionService.createDeposit(deposit).also {
                    logger.info("Deposit with id ${it.id} returned")
                }
            }
            TransactionType.WITHDRAW.name -> {
                val withdraw = Withdraw(
                    originAccount = ObjectId(request.originAccount),
                    userId = ObjectId(request.originAccount),
                    destinyAccount = request.destinyAccount,
                    destinyBankCode = request.toBankCode,
                    amount = request.amount,
                    type = TransactionType.WITHDRAW
                )
                createdTransaction = transactionService.createWithdraw(withdraw).also {
                    logger.info("Withdraw with id ${it.id} returned")
                }
            }
            else -> {
                throw TransactionException("Transaction type ${request.type} is not valid").also {
                    logger.info("Transaction type ${request.type} is not valid")
                }
            }
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

        val transaction = transactionService.getTransfer(ObjectId(transactionId)).also {
            logger.info("Transaction with id ${transactionId} returned")
        }

        ResponseEntity(transaction, HttpStatus.CREATED)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }
}
