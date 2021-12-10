package com.picpay.service

import com.picpay.config.Logger.logger
import com.picpay.exception.TransactionException
import com.picpay.model.request.TransferRequest
import com.picpay.model.response.Transfer
import com.picpay.repository.TransferRepository
import com.picpay.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional

@Service
class TransferService(
    private val transferRepository: TransferRepository,
    private val userRepository: UserRepository
) {

    fun createTransfer(request: TransferRequest): Transfer{
        val transfer = Transfer(
            originAccount = ObjectId(request.originAccount),
            destinyAccount = ObjectId(request.destinyAccount),
            amount = request.amount,
            description = request.description.orEmpty()
        )
        val originAccount = userRepository.findById(transfer.originAccount)
        val destinyAccount = userRepository.findById(transfer.destinyAccount)

        if (!originAccount.isPresent ||
            !destinyAccount.isPresent ||
            originAccount.get().balance < transfer.amount){
            logger.info("")
            throw TransactionException("")
        }

        return transferRepository.save(transfer).also {
            userRepository.saveAll(
                listOf(
                    originAccount.get().copy(
                        balance = originAccount.get().balance - transfer.amount, modifiedDate = LocalDateTime.now()),
                    destinyAccount.get().copy(
                        balance = destinyAccount.get().balance + transfer.amount, modifiedDate = LocalDateTime.now())
                )
            )
        }
    }

    fun getTransfer(transferId: ObjectId): Optional<Transfer>{
        logger.info("Finding transfer with id: $transferId")
        return transferRepository.findById(transferId)
    }
}
