package com.picpay.service

import com.picpay.exception.TransactionException
import com.picpay.model.response.Transfer
import com.picpay.repository.TransferRepository
import com.picpay.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class TransferService(
    private val transferRepository: TransferRepository,
    private val userRepository: UserRepository
) {

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

    fun getTransfer(transferId: ObjectId): Optional<Transfer> = transferRepository.findById(transferId)
}
