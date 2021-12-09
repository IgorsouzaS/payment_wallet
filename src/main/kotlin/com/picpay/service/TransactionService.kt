package com.picpay.service

import com.picpay.exception.TransactionException
import com.picpay.model.response.BankResponse
import com.picpay.model.response.Deposit
import com.picpay.model.response.Transaction
import com.picpay.model.response.Withdraw
import com.picpay.repository.TransactionRepository
import com.picpay.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Service
class TransactionService (
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
){

    fun createDeposit(deposit: Deposit): Deposit = transactionRepository.save(deposit)

    fun createWithdraw(withdraw: Withdraw): Withdraw = runBlocking {

        val res = async { getBankData(withdraw.destinyBankCode!!) }
        val bankData = res.await()

        if (bankData.code == 0) {
            throw TransactionException("the bank code doesn't exist")
        } else {
            transactionRepository.save(withdraw).also {
                val user = userRepository.findById(withdraw.userId).get()
                val updatedUser = user.copy(balance = user.balance - withdraw.amount)
                userRepository.save(updatedUser)
            }
        }
    }

    fun getTransfer(transferId: ObjectId): Transaction{
        return transactionRepository.findById(transferId).get()
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
