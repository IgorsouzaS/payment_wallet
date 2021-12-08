package com.picpay.controller

import com.picpay.model.response.Deposit
import com.picpay.model.response.Transfer
import com.picpay.model.types.TransactionType
import com.picpay.repository.TransactionRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import com.picpay.repository.TransferRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionControllerTest @Autowired constructor(
    private val transferRepository: TransferRepository,
    private val transactionRepository: TransactionRepository,
    private val restTemplate: TestRestTemplate
){

    private val defaultTransactionId = ObjectId.get()
    private val defaultDestinyAccountId = ObjectId.get()
    private val defaultTransferId = ObjectId.get()

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        transactionRepository.deleteAll()
        transferRepository.deleteAll()
    }

    private fun getRootUrl(): String = "http://localhost:$port"

    private fun saveOneTransaction() = transactionRepository.save(
        Deposit(
            id = defaultTransactionId,
            destinyAccount = defaultDestinyAccountId,
            userId = defaultDestinyAccountId,
            amount = Double.fromBits(20L),
            type = TransactionType.DEPOSIT
        )
    )

    @Test
    fun `should create a transfer`() {
        /*
        saveOneTransaction()

        val response = restTemplate.postForEntity(
            getRootUrl() + "/transfers",
            Transfer::class.java
        )

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(defaultTransactionId, response.body?.id)
        */
    }

    @Test
    fun `should return single transfer by id`() {
        saveOneTransaction()

        val response = restTemplate.getForEntity(
            getRootUrl() + "/transfers/$defaultTransferId",
            Transfer::class.java
        )

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(defaultTransferId, response.body?.id)
    }

    @Test
    fun `should create a deposit transaction`() {
        /*
        val response = restTemplate.postForEntity(
            getRootUrl() + "/transactions",
            Transaction::class.java
        )

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(defaultTransactionId, response.body?.id)
         */
    }

    @Test
    fun `should create a withdraw transaction`() {
        /*
        val response = restTemplate.getForEntity(
            getRootUrl() + "/transactions",
            Transaction::class.java
        )

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(defaultTransactionId, response.body?.id)
         */
    }
}
