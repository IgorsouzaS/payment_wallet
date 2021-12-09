package com.picpay.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.picpay.service.TransferService
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

@WebMvcTest
class TransferControllerTest (
    @Autowired private val mockMvc: MockMvc
)  {

    private val mapper = ObjectMapper()

    @MockBean
    lateinit var transferService: TransferService

    @Nested
    inner class TransferCreation {
        @Test
        fun `When create transfer then transfer created with expected amount`() {
            /*
            val transfer = mockDefaultTransfer()
            Mockito.`when`(transferService.createTransfer(transfer)).thenReturn(transfer)
            mockMvc.perform(
                MockMvcRequestBuilders.post("/transfers")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(transfer))
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("amount").value(transfer.amount))
            */
        }
    }


}