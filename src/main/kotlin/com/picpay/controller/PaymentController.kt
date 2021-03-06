package com.picpay.controller

import com.picpay.config.Logger.logger
import com.picpay.exception.TransactionException
import com.picpay.model.request.PaymentRequest
import com.picpay.model.response.Payment
import com.picpay.service.PaymentService
import io.swagger.annotations.ApiOperation
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/payments")
class PaymentController (val paymentService: PaymentService){

    @PostMapping("/")
    @ApiOperation(value = "Create payment", response = Payment::class)
    fun createPayment(@RequestBody request: PaymentRequest) : ResponseEntity<Payment> = try {
        val createdPayment = paymentService.createPayment(request).also {
            logger.info("Payment with id ${it.id} returned")
        }
        ResponseEntity(createdPayment, HttpStatus.CREATED)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get payment by id", response = Payment::class)
    fun getPayment(@PathVariable("id") paymentId: String): ResponseEntity<Payment> = try {
        val payment = paymentService.getPayment(ObjectId(paymentId))
        if(payment.isPresent) {
            logger.info("Payment with id ${payment.get().id} returned")
            ResponseEntity.ok(payment.get())
        }else{
            logger.info("Payment with id $paymentId not found")
            ResponseEntity.notFound().build()
        }
    }catch(e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }
}
