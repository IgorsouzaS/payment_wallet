package com.picpay.service

import com.picpay.model.types.TransactionType
import com.picpay.repository.TransactionRepository
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
class TransactionServiceTest (@Autowired val transactionService: TransactionService) {

    @MockBean
    lateinit var transactionRepository: TransactionRepository

    @Nested
    inner class TransactionCreation {
        @Test
        fun `save transaction to repository`() {
            val deposit = mockDefaultTransaction(TransactionType.DEPOSIT)
            Mockito.`when`(transactionRepository.save(deposit)).thenReturn(deposit)
            val savedTransaction = transactionService.createDeposit(deposit)
            Assertions.assertThat(savedTransaction).isEqualTo(deposit)
        }
    }

    @Nested
    inner class TransactionRetrieve {
        @Test
        fun `retrieve not exist transaction from repository`() {
            val transfer = mockDefaultTransfer("324325325")
            Mockito.`when`(transactionRepository.findById(transfer.id)).thenReturn(null)
            val savedTransfer = transactionService.getTransfer(transfer.id)
            Assertions.assertThat(savedTransfer).isNull()
        }

        @Test
        fun `retrieve saved transaction from repository`() {
            val transfer = mockOptionalTranfer()
            Mockito.`when`(transactionRepository.findById(transfer.id)).thenReturn(transfer)
            val savedTransaction = transactionService.getTransfer(transfer.id)
            Assertions.assertThat(savedTransaction).isEqualTo(transfer)
        }
    }

}
