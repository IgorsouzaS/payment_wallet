package com.picpay.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.picpay.model.request.TransactionRequest
import com.picpay.model.response.Deposit
import com.picpay.model.response.Withdraw
import com.picpay.model.types.TransactionType
import com.picpay.service.TransactionService
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
class TransactionControllerTest (
    @Autowired private val mockMvc: MockMvc
){

    private val mapper = ObjectMapper()
    private val defaultUserId = ObjectId.get()
    private val defaultTransactionId = ObjectId.get()
    private val defaultDestinyAccount = ObjectId.get()

    @MockBean
    lateinit var transactionService: TransactionService

    @Nested
    inner class TransactionCreation {
        @Test
        fun `When create deposit then transaction created with expected id`() {
            val depositRequest = mockTransactionRequest(TransactionType.DEPOSIT.name)
            val deposit = mockDeposit()

            Mockito.`when`(transactionService.createTransaction(depositRequest)).thenReturn(deposit)
            mockMvc.perform(
                MockMvcRequestBuilders.post("/transactions")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(depositRequest))
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(deposit.id))
        }

        @Test
        fun `When create withdraw then transaction created with expected id`() {
            val withdrawRequest = mockTransactionRequest(TransactionType.WITHDRAW.name)
            val withdraw = mockWithdraw()

            Mockito.`when`(transactionService.createTransaction(withdrawRequest)).thenReturn(withdraw)
            mockMvc.perform(
                MockMvcRequestBuilders.post("/transactions")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(withdrawRequest))
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(withdraw.id))
        }
    }


    @Nested
    inner class TransactionRetrieve {

        @Test
        fun `when retrieve transaction while transaction is exists then transaction returns with expected id`() {
            val deposit = mockDeposit()
            Mockito.`when`(transactionService.getTransaction(defaultTransactionId)).thenReturn(Optional.of(deposit))
            mockMvc.perform(
                MockMvcRequestBuilders.get("/transfers/$defaultTransactionId")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(deposit.id))
        }

        @Test
        fun `When retrieve transaction while transaction does not exists then transaction not found`() {
            Mockito.`when`(transactionService.getTransaction(defaultTransactionId)).thenReturn(Optional.empty())
            mockMvc.perform(
                MockMvcRequestBuilders.get("/transactions/$defaultTransactionId")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
        }
    }

    private fun mockTransactionRequest(type: String) = TransactionRequest(
        originAccount = defaultUserId.toString(),
        destinyAccount = defaultDestinyAccount.toString(),
        type = type,
        destinyBankCode = "212",
        amount = Double.fromBits(20L)
    )

    private fun mockDeposit() = Deposit(
        id = defaultTransactionId,
        destinyAccount = defaultDestinyAccount,
        userId = defaultUserId,
        amount = Double.fromBits(20L),
        type = TransactionType.DEPOSIT
    )

    private fun mockWithdraw() = Withdraw(
        id = defaultTransactionId,
        userId = defaultUserId,
        originAccount = defaultUserId,
        destinyAccount = defaultDestinyAccount.toString(),
        amount = Double.fromBits(20L),
        type = TransactionType.DEPOSIT
    )
}
