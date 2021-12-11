package com.picpay.service

import com.picpay.model.request.TransactionRequest
import com.picpay.model.response.Deposit
import com.picpay.model.response.User
import com.picpay.model.response.Withdraw
import com.picpay.model.types.TransactionType
import com.picpay.repository.TransactionRepository
import com.picpay.repository.UserRepository
import org.assertj.core.api.Assertions
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
class TransactionServiceTest (@Autowired val transactionService: TransactionService) {

    private val defaultUserId = ObjectId.get()
    private val defaultTransactionId = ObjectId.get()
    private val defaultDestinyAccount = ObjectId.get()

    @MockBean
    lateinit var transactionRepository: TransactionRepository

    @MockBean
    lateinit var userRepository: UserRepository

    @Nested
    inner class TransactionCreation {
        @Test
        fun `save deposit to repository`() {
            val depositRequest = mockTransactionRequest(TransactionType.DEPOSIT.name)
            val deposit = mockDeposit()
            Mockito.`when`(transactionRepository.save(Mockito.any(Deposit::class.java))).thenReturn(deposit)
            val savedTransaction = transactionService.createTransaction(depositRequest)
            Assertions.assertThat(savedTransaction).isEqualTo(deposit)
        }

        @Test
        fun `save withdraw to repository`() {
            val withdrawRequest = mockTransactionRequest(TransactionType.WITHDRAW.name)
            val withdraw = mockWithdraw()
            val user = mockUser()
            Mockito.`when`(userRepository.findById(defaultUserId)).thenReturn(Optional.of(user))
            Mockito.`when`(userRepository.save(Mockito.any(User::class.java))).thenReturn(user.copy(balance = user.balance - withdraw.amount))
            Mockito.`when`(transactionRepository.save(Mockito.any(Withdraw::class.java))).thenReturn(withdraw)
            val savedTransaction = transactionService.createTransaction(withdrawRequest)
            Assertions.assertThat(savedTransaction).isEqualTo(withdraw)
        }
    }

    @Nested
    inner class TransactionRetrieve {
        @Test
        fun `retrieve saved transaction from repository`() {
            val deposit = mockDeposit()
            Mockito.`when`(transactionRepository.findById(defaultTransactionId)).thenReturn(Optional.of(deposit))
            val savedTransaction = transactionService.getTransaction(defaultTransactionId)
            Assertions.assertThat(savedTransaction).isEqualTo(deposit)
        }

        @Test
        fun `retrieve not exist transaction from repository`() {
            Mockito.`when`(transactionRepository.findById(Mockito.any(ObjectId::class.java))).thenReturn(Optional.empty())
            val savedTransaction = transactionService.getTransaction(defaultTransactionId)
            Assertions.assertThat(savedTransaction.isPresent).isFalse()
        }
    }

    private fun mockTransactionRequest(type: String) = TransactionRequest(
        originAccount = defaultUserId.toString(),
        destinyAccount = defaultDestinyAccount.toString(),
        type = type,
        destinyBankCode = "212",
        amount = Double.fromBits(20L)
    )

    private fun mockUser() =  User(
        id = defaultUserId,
        balance = Double.fromBits(20L),
        email = "email@test.com"
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
