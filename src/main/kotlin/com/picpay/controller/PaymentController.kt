package com.picpay.controller

import com.picpay.exception.TransactionException
import com.picpay.model.request.PaymentRequest
import com.picpay.model.response.Payment
import com.picpay.common.Logger.logger
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.picpay.service.PaymentService

@RestController("/payments")
class PaymentController (val paymentService: PaymentService){

    @PostMapping("/")
    fun createPayment(@RequestBody request: PaymentRequest) : ResponseEntity<Payment> = try {
        val payment = Payment(
            barcode = request.barcode,
            amount = request.amount,
            userId = ObjectId(request.userId),
            description = request.description.orEmpty()
        )
        val createdPayment = paymentService.createPayment(payment).also {
            logger.info("Payment with id ${it.id} returned")
        }
        ResponseEntity(createdPayment, HttpStatus.CREATED)
    }catch (e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }

    @GetMapping("/{id}")
    fun getPayment(@PathVariable("id") paymentId: String): ResponseEntity<Payment> = try {
        val payment = paymentService.getPayment(ObjectId(paymentId)).also {
            logger.info("Payment with id ${it.get().id} returned")
        }
        ResponseEntity.ok(payment.get())
    }catch(e: Exception) {
        throw TransactionException("Unexpected Error: ${e.message}").also {
            logger.info("Unexpected Error: ${e.message}")
        }
    }
}
