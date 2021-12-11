package com.picpay.service

import com.picpay.config.Logger.logger
import com.picpay.exception.TransactionException
import com.picpay.model.request.TransactionRequest
import com.picpay.model.response.BankResponse
import com.picpay.model.response.Deposit
import com.picpay.model.response.Transaction
import com.picpay.model.response.Withdraw
import com.picpay.model.types.TransactionType
import com.picpay.repository.TransactionRepository
import com.picpay.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Optional

@Service
class TransactionService (
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
){

    fun createTransaction(request: TransactionRequest): Transaction{
        when(request.type) {
            TransactionType.DEPOSIT.name -> {
                val deposit = Deposit(
                    amount = request.amount,
                    destinyAccount = ObjectId(request.destinyAccount),
                    userId = ObjectId(request.destinyAccount),
                    type = TransactionType.DEPOSIT
                )
                return createDeposit(deposit).also {
                    logger.info("Deposit created with id ${it.id}")
                }
            }
            TransactionType.WITHDRAW.name -> {
                val withdraw = Withdraw(
                    originAccount = ObjectId(request.originAccount),
                    userId = ObjectId(request.originAccount),
                    destinyAccount = request.destinyAccount,
                    destinyBankCode = request.destinyBankCode,
                    amount = request.amount,
                    type = TransactionType.WITHDRAW
                )
                return createWithdraw(withdraw).also {
                    logger.info("Withdraw created with id ${it.id}")
                }
            }
            else -> {
                throw TransactionException("Transaction type ${request.type} is not valid").also {
                    logger.info("Transaction type ${request.type} is not valid")
                }
            }
        }
    }

    private fun createDeposit(deposit: Deposit): Deposit = transactionRepository.save(deposit)

    private fun createWithdraw(withdraw: Withdraw): Withdraw = runBlocking {

        val res = async { getBankData(withdraw.destinyBankCode!!) }
        val bankData = res.await()

        if (bankData.code == 0) {
            throw TransactionException("The bank code doesn't exist")
        } else {
            transactionRepository.save(withdraw).also {
                val user = userRepository.findById(withdraw.userId).get()
                val updatedUser = user.copy(balance = user.balance - withdraw.amount)
                userRepository.save(updatedUser)
            }
        }
    }

    fun getTransaction(transactionId: ObjectId): Optional<Transaction> {
        return transactionRepository.findById(transactionId)
    }

    private fun getBankData(code: String): BankResponse {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://brasilapi.com.br/api/banks/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(BankService::class.java)
        val call = service.getBankData(code)
        return call.execute().body()
    }
}
