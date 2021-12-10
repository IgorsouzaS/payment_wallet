package com.picpay

import com.picpay.model.request.TransferRequest
import com.picpay.model.response.Transfer
import com.picpay.repository.TransferRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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
    private val defaultDestinyId = ObjectId.get()

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        transferRepository.deleteAll()
    }

    private val transferRequest = mockTransferRequest()
    private val transfer = mockTransfer()

    private fun getRootUrl(): String = "http://localhost:$port/transfers"

    private fun saveTransfer() = transferRepository.save(transfer)

    @Test
    fun `should create a transfer`() {
        val response = restTemplate.postForEntity(getRootUrl(), transferRequest, Transfer::class.java)

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(defaultTransferId, response.body?.id)
    }

    @Test
    fun `should return single transfer by id`() {
        saveTransfer()

        val response = restTemplate.getForEntity(getRootUrl() + "/$defaultTransferId", Transfer::class.java)

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(defaultTransferId, response.body?.id)
    }

    private fun mockTransfer() = Transfer(
        id = defaultTransferId,
        destinyAccount = defaultDestinyId,
        originAccount = defaultUserId,
        amount = Double.fromBits(20L),
        description = "Payment test"
    )

    private fun mockTransferRequest() = TransferRequest(
        destinyAccount = defaultDestinyId.toString(),
        originAccount = defaultUserId.toString(),
        amount = Double.fromBits(20L),
        description = "Payment test"
    )
}
