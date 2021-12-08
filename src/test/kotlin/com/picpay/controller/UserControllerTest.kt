package com.picpay.controller

import com.picpay.repository.PaymentRepository
import com.picpay.repository.TransactionRepository
import com.picpay.repository.TransferRepository
import com.picpay.repository.UserRepository
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
class UserControllerTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val transferRepository: TransferRepository,
    private val transactionRepository: TransactionRepository,
    private val paymentRepository: PaymentRepository,
    private val restTemplate: TestRestTemplate
){

    private val defaultPaymentId = ObjectId.get()
    private val defaultTransferId = ObjectId.get()
    private val defaultTransactionId = ObjectId.get()
    private val defaultUserId = ObjectId.get()

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        paymentRepository.deleteAll()
        transferRepository.deleteAll()
        transactionRepository.deleteAll()
    }

    private fun getRootUrl(): String = "http://localhost:$port"

    @Test
    fun `should return all user transactions`() {
        /*
        saveOneTransaction()

        val response = restTemplate.getForEntity(
            getRootUrl(),
            List::class.java
        )

        Assertions.assertEquals(200, response.statusCode.value())
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(1, response.body?.size)
        */
    }

    @Test
    fun `should return all user transfers`() {
        /*
        saveOneTransaction()

        val response = restTemplate.getForEntity(
            getRootUrl(),
            List::class.java
        )

        Assertions.assertEquals(200, response.statusCode.value())
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(1, response.body?.size)
        */
    }

    @Test
    fun `should return all user payments`() {
        /*
        saveOneTransaction()

        val response = restTemplate.getForEntity(
            getRootUrl(),
            List::class.java
        )

        Assertions.assertEquals(200, response.statusCode.value())
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(1, response.body?.size)
        */
    }
}