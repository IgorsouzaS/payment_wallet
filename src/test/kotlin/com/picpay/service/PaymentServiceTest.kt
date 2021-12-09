package com.picpay.service

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

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class PaymentServiceTest(@Autowired val paymentService: PaymentService) {

    @MockBean
    lateinit var paymentRepository: PaymentRepository

    @Nested
    inner class PaymentCreation {
        @Test
        fun `save payment to repository`() {
            val payment = mockDefaultPayment()
            Mockito.`when`(paymentRepository.save(payment)).thenReturn(payment)
            val savedPayment = paymentService.createPayment(payment)
            assertThat(savedPayment).isEqualTo(payment)
        }
    }

    @Nested
    inner class PaymentRetrieve {
        @Test
        fun `retrieve not exist payment from repository`() {
            val payment = mockDefaultPayment("324325325")
            Mockito.`when`(paymentRepository.findById(payment.id)).thenReturn(null)
            val savedPayment = paymentService.getPayment(payment.id)
            assertThat(savedPayment).isNull()
        }

        @Test
        fun `retrieve saved payment from repository`() {
            val payment = mockOptionalPayment()
            Mockito.`when`(paymentRepository.findById(payment.id)).thenReturn()
            val savedPayment = paymentService.getPayment(payment.id)
            assertThat(savedPayment).isEqualTo(payment)
        }
    }

    private fun mockDefaultPayment(id: String? = null) =  Payment(
        id = if (id == null) {
            ObjectId.get()
        }else{
            ObjectId(id)
        },
        userId = ObjectId.get(),
        barcode = "",
        amount = Double.fromBits(20L),
        description = ""
    )
}
