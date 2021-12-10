package com.picpay.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.picpay.model.request.PaymentRequest
import com.picpay.model.response.Payment
import com.picpay.service.PaymentService
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Optional

@WebMvcTest
class PaymentControllerTest (
    @Autowired private val mockMvc: MockMvc
) {

    private val mapper = ObjectMapper()
    private val defaultPaymentId = ObjectId.get()
    private val defaultUserId = ObjectId.get()

    @MockBean
    lateinit var paymentService: PaymentService

    @Nested
    inner class PaymentCreation {
        @Test
        fun `When create payment then payment created with expected amount`() {
            val paymentRequest = mockPaymentRequest()
            val payment = mockPayment()
            Mockito.`when`(paymentService.createPayment(paymentRequest)).thenReturn(payment)
            mockMvc.perform(
                post("/payments")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(paymentRequest))
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isCreated)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("amount").value(payment.amount))
        }
    }

    @Nested
    inner class PaymentRetrieve {

        @Test
        fun `when retrieve payment while payment is exists then payment returns with expected id`() {
            val payment = mockPayment()
            Mockito.`when`(paymentService.getPayment(payment.id)).thenReturn(Optional.of(payment))
            mockMvc.perform(
                get("/payments/${payment.id}")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(payment.id))
        }

        @Test
        fun `When retrieve payment while payment does not exists then payment not found`() {

            Mockito.`when`(paymentService.getPayment(defaultPaymentId)).thenReturn(Optional.empty())
            mockMvc.perform(get("/payments/${defaultPaymentId}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)
        }
    }

    private fun mockPayment() = Payment(
        id = defaultPaymentId,
        userId = defaultUserId,
        barcode = "123456789012",
        amount = Double.fromBits(20L),
        description = "Pagamento teste"
    )

    private fun mockPaymentRequest() = PaymentRequest(
        userId = defaultUserId.toString(),
        barcode = "123456789012",
        amount = Double.fromBits(20L),
        description = "Payment test"
    )
}
