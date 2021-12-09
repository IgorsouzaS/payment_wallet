package com.picpay.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.picpay.model.response.Transaction
import com.picpay.service.TransactionService
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.internal.matchers.Any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
class TransactionControllerTest (
    @Autowired private val mockMvc: MockMvc
){
    private val mapper = ObjectMapper()

    @MockBean
    lateinit var transactionService: TransactionService

    @Nested
    inner class TransactionCreation {
        @Test
        fun `When create deposit then transaction created with expected amount`() {
            val transaction = mockDefaultTransaction()   //DEVE SER PAYMENT REQUEST ????
            Mockito.`when`(transactionService.createDeposit(transaction)).thenReturn(transaction)
            mockMvc.perform(
                MockMvcRequestBuilders.post("/transactions")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(transaction))
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("amount").value(transaction.amount))
        }

        @Test
        fun `When create withdraw then transaction created with expected amount`() {
            val transaction = mockDefaultTransaction()
            Mockito.`when`(transactionService.createWithdraw(transaction)).thenReturn(transaction)
            mockMvc.perform(
                MockMvcRequestBuilders.post("/transactions")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(transaction))
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("amount").value(transaction.amount))
        }
    }


    @Nested
    inner class TransactionRetrieve {

        @Test
        fun `when retrieve transfer while transfer is exists then transfer returns with expected id`() {
            val transfer = mockDefaultTransfer()
            Mockito.`when`(transactionService.getTransfer(transfer.id)).thenReturn(transfer)
            mockMvc.perform(
                MockMvcRequestBuilders.get("/transfers")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(transfer.id))
        }

/*
        @Test
        fun `When retrieve transaction while transaction does not exists then transaction not found`() {
            Mockito.`when`(transactionService.getTransaction(ObjectId(Any.ANY.toString()))).thenReturn(null)
            mockMvc.perform(
                MockMvcRequestBuilders.get("/transactions")
                .param("type", "deposit")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
        }
*/
        @Test
        fun `When retrieve transfer while transfer does not exists then transfer not found`() {
            Mockito.`when`(transactionService.getTransfer(ObjectId(Any.ANY.toString()))).thenReturn(null)
            mockMvc.perform(
                MockMvcRequestBuilders.get("/transfers")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
        }
    }

    private fun mockDefaultTransaction() = Transaction(

    )

}
