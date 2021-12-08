package com.picpay.service

import com.picpay.exception.TransactionException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import com.picpay.model.response.BankResponse
import com.picpay.model.response.Deposit
import com.picpay.model.response.Transfer
import com.picpay.model.response.Withdraw
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import com.picpay.repository.TransferRepository
import org.springframework.stereotype.Service
import com.picpay.repository.TransactionRepository
import com.picpay.repository.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Optional

@Service
class TransactionService (
    private val transferRepository: TransferRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
){

    fun getTransfer(transferId: ObjectId): Optional<Transfer> = transferRepository.findById(transferId)

    fun createTransfer(transfer: Transfer): Transfer{
        val originAccount = userRepository.findByIdOrNull(transfer.originAccount)
        val destinyAccount = userRepository.findByIdOrNull(transfer.destinyAccount)

        if (originAccount!!.balance > 0 &&
            originAccount.balance >= transfer.amount &&
            destinyAccount != null){
            throw TransactionException("")
        }

        userRepository.saveAll(
            listOf(
                originAccount.copy(balance = originAccount.balance - transfer.amount),
                destinyAccount!!.copy(balance = destinyAccount.balance + transfer.amount)
            )
        )
        return transferRepository.save(transfer)
    }


    fun createDeposit(deposit: Deposit): Deposit = transactionRepository.save(deposit)
    fun createWithdraw(withdraw: Withdraw): Withdraw = runBlocking {

        val res = async { getBankData(withdraw.destinyBankCode!!) }
        val bankData = res.await()

        if (bankData.code == 0) {
            throw TransactionException("the bank code doesn't exist")
        } else {
            transactionRepository.save(withdraw)
        }
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
