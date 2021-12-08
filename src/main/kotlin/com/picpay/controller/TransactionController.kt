package com.picpay.controller

import com.picpay.exception.TransactionException
import com.picpay.model.request.TransactionRequest
import com.picpay.model.request.TransferRequest
import com.picpay.model.response.Deposit
import com.picpay.model.response.Transaction
import com.picpay.model.response.Transfer
import com.picpay.model.response.Withdraw
import com.picpay.config.Logger.logger
import com.picpay.model.types.TransactionType
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.picpay.service.TransactionService
import io.swagger.annotations.ApiOperation

@RestController("/")
class TransactionController(val transactionService: TransactionService) {

    @PostMapping("/transfers")
    @ApiOperation(value = "Create transfer", response = Transfer::class)
    fun createTransfer(@RequestBody request: TransferRequest) : ResponseEntity<Transfer> = try {
        val transfer = Transfer(
            originAccount = request.originAccount,
            destinyAccount = request.destinyAccount,
            amount = request.amount,
            description = request.description.orEmpty()
        )
        val createdTransaction = transactionService.createTransfer(transfer).also {
            logger.info("Transaction with id ${it.id} returned")
        }
        ResponseEntity(createdTransaction, HttpStatus.CREATED)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/transfers/{id}")
    @ApiOperation(value = "Get Transfers by Id", response = Transfer::class)
    fun getTransfer(@PathVariable("id") transferId: String): ResponseEntity<Transfer> = try {
        val transaction = transactionService.getTransfer(ObjectId(transferId)).also {
            logger.info("Transaction with id ${it.get().id} returned")
        }
        ResponseEntity.ok(transaction.get())
    }catch(e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @PostMapping("/transactions")
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
}
