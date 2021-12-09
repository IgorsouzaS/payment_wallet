package com.picpay.controller

import com.picpay.config.Logger
import com.picpay.exception.TransactionException
import com.picpay.model.request.TransferRequest
import com.picpay.model.response.Transfer
import com.picpay.service.TransferService
import io.swagger.annotations.ApiOperation
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/")
class TransferController(val transferService: TransferService) {

    @PostMapping("/transfers")
    @ApiOperation(value = "Create transfer", response = Transfer::class)
    fun createTransfer(@RequestBody request: TransferRequest) : ResponseEntity<Transfer> = try {
        val transfer = Transfer(
            originAccount = request.originAccount,
            destinyAccount = request.destinyAccount,
            amount = request.amount,
            description = request.description.orEmpty()
        )
        val createdTransaction = transferService.createTransfer(transfer).also {
            Logger.logger.info("Transfer with id ${it.id} returned")
        }
        ResponseEntity(createdTransaction, HttpStatus.CREATED)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            Logger.logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/transfers/{id}")
    @ApiOperation(value = "Get Transfers by Id", response = Transfer::class)
    fun getTransfer(@PathVariable("id") transferId: String): ResponseEntity<Transfer> = try {
        val transaction = transferService.getTransfer(ObjectId(transferId)).also {
            Logger.logger.info("Transfer with id ${it.get().id} returned")
        }
        ResponseEntity.ok(transaction.get())
    }catch(e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            Logger.logger.info("Unexpected Error: ${e.message}")
        }
    }
}
