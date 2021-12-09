package com.picpay

import com.picpay.model.request.PaymentRequest
import com.picpay.model.response.Payment
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import com.picpay.repository.PaymentRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentIntegrationTest @Autowired constructor(
    private val paymentRepository: PaymentRepository,
    private val restTemplate: TestRestTemplate
) {

    private val defaultPaymentId = ObjectId.get()
    private val defaultUserId = ObjectId.get()

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        paymentRepository.deleteAll()
    }

    private val payment = Payment(
        id = defaultPaymentId,
        userId = defaultUserId,
        barcode = "123456789012",
        amount = Double.fromBits(20L),
        description = "Pagamento teste"
    )

    private fun getRootUrl(): String = "http://localhost:$port/payments"

    private fun saveOnePayment() = paymentRepository.save(payment)

    @Test
    fun `should return a payment by id`() {
        saveOnePayment()
        val url = "${getRootUrl()}/${defaultUserId}"

        val response = restTemplate.getForEntity(
            url,
            Payment::class.java
        )

        Assertions.assertEquals(200, response.statusCode.value())
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(defaultPaymentId, response.body?.id)
    }

    @Test
    fun `should create a payment`() {
        saveOnePayment()
        val url = getRootUrl()
        val barcode = "123456789"

        val response = restTemplate.postForEntity(
            url,
            PaymentRequest(
                userId = defaultUserId.toString(),
                barcode = barcode,
                amount = Double.fromBits(20L)
            ),
            Payment::class.java
        )

        Assertions.assertEquals(200, response.statusCode.value())
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(defaultUserId, response.body!!.userId)
    }
}
