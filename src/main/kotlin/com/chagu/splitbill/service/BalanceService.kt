package com.chagu.splitbill.service

import com.chagu.splitbill.dto.response.BalanceResponseDto
import com.chagu.splitbill.dto.response.IndividualBalanceResponseDto
import com.chagu.splitbill.dto.response.OtherOwesDto
import com.chagu.splitbill.dto.response.UserOwesDto
import com.chagu.splitbill.enum.ErrorMessages
import com.chagu.splitbill.exceptions.BillNotFoundException
import com.chagu.splitbill.model.BillEntity
import com.chagu.splitbill.repository.BillRepository
import com.chagu.splitbill.utils.LoggerProvider
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class BalanceService(private val billRepository: BillRepository, private val userService: UserService) {

    companion object {
        val LOG: Logger = LoggerProvider.getLogger(BalanceService::class.java)
    }

    fun getBalanceByUserId(userId: Int): BalanceResponseDto {
        var billList: List<BillEntity> = billRepository.findListOfBillsForBalance(userId)
        if (billList.isEmpty())
            throw BillNotFoundException("${ErrorMessages.NO_RECORD_FOUND.message} for userId : $userId")
        var userEntity = userService.findUserById(userId)
        var returnDto = BalanceResponseDto()
        returnDto.userName = userEntity.fullName
        returnDto.email = userEntity.email

        var owesToList: MutableList<UserOwesDto> = mutableListOf()
        var otherUserOwesList: MutableList<OtherOwesDto> = mutableListOf()
        var totalAmountToGive: Long = 0
        var totalAmountToGet: Long = 0

        for (bill in billList) {
            for (split in bill.splitDetails) {
                if (split.paidBy?.id == userId) {  // person had paid
                    var newBalance = OtherOwesDto()
                    newBalance.userId = split.paidTo?.id
                    newBalance.userName = "${split.paidTo?.firstName} ${split.paidTo?.lastName}"
                    newBalance.amountOwes = split.amountPaid
                    otherUserOwesList.add(newBalance)
                    totalAmountToGet += split.amountPaid!!
                } else {  // person had borrowed
                    var owesTo = UserOwesDto()
                    owesTo.userId = split.paidBy?.id
                    owesTo.userName = "${split.paidBy?.firstName} ${split.paidBy?.lastName}"
                    owesTo.amountToGive = split.amountPaid
                    owesToList.add(owesTo)
                    totalAmountToGive += split.amountPaid!!
                }
            }
        }
        returnDto.otherUserOwes = otherUserOwesList
        returnDto.owesTo = owesToList
        returnDto.totalAmountToGet = totalAmountToGet
        returnDto.totalAmountToGive = totalAmountToGive

        return returnDto
    }

    fun getIndividualBalance(userId: Int, searchUserId: Int): IndividualBalanceResponseDto {
        var billList: List<BillEntity> = billRepository.findListOfBillsForIndividualBalance(userId, searchUserId)
        if (billList.isEmpty())
            throw BillNotFoundException(ErrorMessages.NO_RECORD_FOUND.message)
        var userEntity = userService.findUserById(userId)
        var searchedUserEntity = userService.findUserById(searchUserId)
        var returnDto = IndividualBalanceResponseDto()
        returnDto.userId = userEntity.id
        returnDto.userName = userEntity.fullName
        returnDto.searchedUserId = searchedUserEntity.id
        returnDto.searchedUserName = searchedUserEntity.fullName

        var totalAmountToGive: Long = 0
        var totalAmountToGet: Long = 0

        for (bill in billList) {
            for (split in bill.splitDetails) {
                if (split.paidBy?.id == userId) {                  // person had paid
                    totalAmountToGet += split.amountPaid!!
                } else {                                           // person had borrowed
                    totalAmountToGive += split.amountPaid!!
                }
            }
        }
        returnDto.totalAmountToGet = totalAmountToGet
        returnDto.totalAmountToGive = totalAmountToGive

        return returnDto
    }
}