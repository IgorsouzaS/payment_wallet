package com.picpay

import com.picpay.model.response.Transfer
import com.picpay.repository.TransferRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferIntegrationTest (
    private val transferRepository: TransferRepository,
    private val restTemplate: TestRestTemplate
) {

    private val defaultTransferId = ObjectId.get()
    private val defaultUserId = ObjectId.get()

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        transferRepository.deleteAll()
    }

    private val transfer = Transfer(
        id = defaultTransferId,
        destinyAccount = ObjectId.get(),
        originAccount = defaultUserId,
        amount = Double.fromBits(20L),
        description = "Pagamento teste"
    )

    private fun getRootUrl(): String = "http://localhost:$port/transfers"

    private fun saveOneTransfer() = transferRepository.save(transfer)

    @Test
    fun `should create a transfer`() {
        /*
        saveOneTransfer()

        val response = restTemplate.postForEntity(
            getRootUrl(),
            Transfer::class.java
        )

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(defaultTransactionId, response.body?.id)
        */
    }

    @Test
    fun `should return single transfer by id`() {
        /*
        saveOneTransfer()

        val response = restTemplate.getForEntity(
            getRootUrl() + "/$defaultTransferId",
            Transfer::class.java
        )

        Assertions.assertEquals(200, response.statusCode.value())
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(defaultTransferId, response.body?.id)
        */
    }
}
