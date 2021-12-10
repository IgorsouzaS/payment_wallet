package com.picpay.service

import com.picpay.config.Logger.logger
import com.picpay.exception.TransactionException
import com.picpay.model.request.PaymentRequest
import com.picpay.model.response.Payment
import com.picpay.repository.PaymentRepository
import com.picpay.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional

@Service
class PaymentService (
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository
){
    fun createPayment(request: PaymentRequest): Payment {

        val user = userRepository.findById(ObjectId(request.userId))

        if (!user.isPresent || user.get().balance < request.amount){
            logger.info("")
            throw TransactionException("")
        }

        val payment = Payment(
            barcode = request.barcode,
            amount = request.amount,
            userId = ObjectId(request.userId),
            description = request.description.orEmpty()
        )
        logger.info("Saving payment: $payment")
        return paymentRepository.save(payment).also {
            userRepository.save(user.get().copy(
                balance = user.get().balance - payment.amount,
                modifiedDate = LocalDateTime.now()
            ))
        }
    }

    fun getPayment(paymentId: ObjectId): Optional<Payment> {
        logger.info("Finding payment with id: $paymentId")
        return paymentRepository.findById(paymentId)
    }
}
