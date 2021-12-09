package com.picpay.service

import com.picpay.repository.TransferRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class TransferServiceTest (@Autowired val transferService: TransferService){

    @MockBean
    lateinit var transferRepository: TransferRepository

    @Nested
    inner class TransferCreation {
        @Test
        fun `save transfer to repository`() {
            /*
            val transfer = mockDefaultTransfer()
            Mockito.`when`(transferRepository.save(transfer)).thenReturn(transfer)
            val savedTransfer = transferService.createTransfer(transfer)
            Assertions.assertThat(savedTransfer).isEqualTo(transfer)
            */
        }
    }

    @Nested
    inner class TransferRetrieve {
        @Test
        fun `retrieve not exist transaction from repository`() {
            /*
            val transfer = mockDefaultTransfer("324325325")
            Mockito.`when`(transferRepository.findById(transfer.id)).thenReturn(null)
            val savedTransfer = transferService.getTransfer(transfer.id)
            Assertions.assertThat(savedTransfer).isNull()
            */
        }

        @Test
        fun `retrieve saved transaction from repository`() {
            /*
            val transfer = mockOptionalTranfer()
            Mockito.`when`(transferRepository.findById(transfer.id)).thenReturn(transfer)
            val savedTransaction = transferService.getTransfer(transfer.id)
            Assertions.assertThat(savedTransaction).isEqualTo(transfer)
            */
        }
    }

}
