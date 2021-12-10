package com.picpay.service

import com.picpay.model.request.TransferRequest
import com.picpay.model.response.Transfer
import com.picpay.model.response.User
import com.picpay.repository.TransferRepository
import com.picpay.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Optional

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class TransferServiceTest (@Autowired val transferService: TransferService){

    private val defaultTransferId = ObjectId.get()
    private val defaultOriginAccount = ObjectId.get()
    private val defaultDestinyAccount = ObjectId.get()

    @MockBean
    lateinit var transferRepository: TransferRepository

    @MockBean
    lateinit var userRepository: UserRepository

    @Nested
    inner class TransferCreation {
        @Test
        fun `save transfer to repository`() {
            val transfer = mockTransfer()
            val transferRequest = mockTransferRequest()
            val user = mockUser()
            val updatedUser = mockUser().copy(balance = user.balance - transferRequest.amount)

            Mockito.`when`(userRepository.findById(defaultOriginAccount)).thenReturn(Optional.of(user))
            Mockito.`when`(userRepository.save(user)).thenReturn(updatedUser)
            Mockito.`when`(transferRepository.save(transfer)).thenReturn(transfer)
            val savedTransfer = transferService.createTransfer(transferRequest)
            assertThat(savedTransfer).isEqualTo(transfer)
        }
    }

    @Nested
    inner class TransferRetrieve {
        @Test
        fun `retrieve not exist transfer from repository`() {
            Mockito.`when`(transferRepository.findById(defaultTransferId)).thenReturn(Optional.empty())
            val savedTransfer = transferService.getTransfer(defaultTransferId)
            assertThat(savedTransfer.isPresent).isFalse()
        }

        @Test
        fun `retrieve saved transfer from repository`() {
            val transfer = mockTransfer()
            Mockito.`when`(transferRepository.findById(transfer.id)).thenReturn(Optional.of(transfer))
            val savedTransaction = transferService.getTransfer(transfer.id)
            assertThat(savedTransaction.isPresent).isTrue()
            assertThat(savedTransaction.get().id).isEqualTo(transfer.id)
        }
    }

    private fun mockUser() =  User(
        id = defaultOriginAccount,
        balance = Double.fromBits(20L),
        email = "email@test.com"
    )

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
