package com.picpay.service

import com.picpay.model.response.Payment
import com.picpay.common.Logger.logger
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import com.picpay.repository.PaymentRepository
import java.util.Optional

@Service
class PaymentService (
    private val repository: PaymentRepository
){
    fun createPayment(payment: Payment): Payment {
        logger.info("Saving payment: $payment")
        return repository.save(payment)
    }

    fun getPayment(paymentId: ObjectId): Optional<Payment> {
        logger.info("Finding payment with id: $paymentId")
        return repository.findById(paymentId)
    }
}