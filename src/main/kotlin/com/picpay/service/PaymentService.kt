package com.picpay.service

import com.picpay.config.Logger.logger
import com.picpay.model.request.PaymentRequest
import com.picpay.model.response.Payment
import com.picpay.repository.PaymentRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentService (
    private val repository: PaymentRepository
){
    fun createPayment(request: PaymentRequest): Payment {
        val payment = Payment(
            barcode = request.barcode,
            amount = request.amount,
            userId = ObjectId(request.userId),
            description = request.description.orEmpty()
        )
        logger.info("Saving payment: $payment")
        return repository.save(payment)
    }

    fun getPayment(paymentId: ObjectId): Optional<Payment> {
        logger.info("Finding payment with id: $paymentId")
        return repository.findById(paymentId)
    }
}
