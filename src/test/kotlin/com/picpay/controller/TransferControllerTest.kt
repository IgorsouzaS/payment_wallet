package com.picpay.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.picpay.model.request.TransferRequest
import com.picpay.model.response.Transfer
import com.picpay.service.TransferService
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.Optional

@WebMvcTest
class TransferControllerTest (
    @Autowired private val mockMvc: MockMvc
)  {

    private val defaultTransferId = ObjectId.get()
    private val defaultOriginAccount = ObjectId.get()
    private val defaultDestinyAccount = ObjectId.get()
    private val mapper = ObjectMapper()

    @MockBean
    lateinit var transferService: TransferService

    @Nested
    inner class TransferCreation {
        @Test
        fun `When create transfer then transfer created with expected id`() {
            val transferRequest = mockTransferRequest()
            val transfer = mockTransfer()

            Mockito.`when`(transferService.createTransfer(transferRequest)).thenReturn(transfer)
            mockMvc.perform(
                MockMvcRequestBuilders.post("/transfers")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(transferRequest))
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(transfer.id))
        }
    }

    @Nested
    inner class TransferRetrieve {

        @Test
        fun `when retrieve transfer while transfer is exists then transfer returns with expected id`() {
            val transfer = mockTransfer()

            Mockito.`when`(transferService.getTransfer(defaultTransferId)).thenReturn(Optional.of(transfer))
            mockMvc.perform(
                MockMvcRequestBuilders.get("/transfers/${defaultTransferId}")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(defaultTransferId))
        }

        @Test
        fun `When retrieve transfer while transfer does not exists then transfer not found`() {
            Mockito.`when`(transferService.getTransfer(Mockito.any(ObjectId::class.java))).thenReturn(Optional.empty())
            mockMvc.perform(
                MockMvcRequestBuilders.get("/transfers/${defaultTransferId}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
        }
    }

    private fun mockTransfer() = Transfer(
        id = defaultTransferId,
        originAccount = defaultOriginAccount,
        destinyAccount = defaultDestinyAccount,
        description = "Payment test",
        amount = Double.fromBits(20L)
    )

    private fun mockTransferRequest() = TransferRequest(
        originAccount = defaultOriginAccount.toString(),
        destinyAccount = defaultDestinyAccount.toString(),
        amount = Double.fromBits(20L),
        description = "Payment test"
    )
}
