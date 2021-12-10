package com.picpay

import com.picpay.model.request.PaymentRequest
import com.picpay.model.response.Payment
import com.picpay.repository.PaymentRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

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

    private val paymentRequest = mockPaymentRequest()
    private val payment = mockPayment()

    private fun getRootUrl(): String = "http://localhost:$port/payments"

    private fun savePayment() = paymentRepository.save(payment)

    @Test
    fun `should create a payment`() {
        val url = getRootUrl()

        val response = restTemplate.postForEntity(url, paymentRequest, Payment::class.java)

        Assertions.assertEquals(200, response.statusCode.value())
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(defaultPaymentId, response.body!!.id)
    }

    @Test
    fun `should return a payment by id`() {
        savePayment()
        val url = "${getRootUrl()}/${defaultPaymentId}"

        val response = restTemplate.getForEntity(url, Payment::class.java)

        Assertions.assertEquals(200, response.statusCode.value())
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(defaultPaymentId, response.body?.id)
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
