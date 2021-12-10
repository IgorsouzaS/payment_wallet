package com.picpay.service

import com.picpay.model.request.PaymentRequest
import com.picpay.model.response.Payment
import com.picpay.repository.PaymentRepository
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Optional

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class PaymentServiceTest(@Autowired val paymentService: PaymentService) {

    private val defaultPaymentId = ObjectId.get()
    private val defaultUserId = ObjectId.get()

    @MockBean
    lateinit var paymentRepository: PaymentRepository

    @Nested
    inner class PaymentCreation {
        @Test
        fun `save payment to repository`() {
            val paymentRequest = mockPaymentRequest()
            val payment = mockPayment()
            Mockito.`when`(paymentRepository.save(Mockito.any(Payment::class.java))).thenReturn(payment)
            val savedPayment = paymentService.createPayment(paymentRequest)
            assertThat(savedPayment).isEqualTo(payment)
        }
    }

    @Nested
    inner class PaymentRetrieve {
        @Test
        fun `retrieve not exist payment from repository`() {
            val payment = mockPayment()
            Mockito.`when`(paymentRepository.findById(payment.id)).thenReturn(Optional.empty())
            val savedPayment = paymentService.getPayment(payment.id)
            assertThat(savedPayment.isPresent).isFalse()
        }

        @Test
        fun `retrieve saved payment from repository`() {
            val payment = mockPayment()
            Mockito.`when`(paymentRepository.findById(payment.id)).thenReturn(Optional.of(payment))
            val savedPayment = paymentService.getPayment(payment.id)
            assertThat(savedPayment).isEqualTo(payment)
        }
    }

    private fun mockPayment() =  Payment(
        id = defaultPaymentId,
        userId = defaultUserId,
        barcode = "123456789012",
        amount = Double.fromBits(20L),
        description = "Payment test"
    )

    private fun mockPaymentRequest() = PaymentRequest(
        userId = defaultUserId.toString(),
        barcode = "123456789012",
        amount = Double.fromBits(20L),
        description = "Payment test"
    )
}
