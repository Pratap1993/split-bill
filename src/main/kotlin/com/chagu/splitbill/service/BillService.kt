package com.chagu.splitbill.service

import com.chagu.splitbill.dto.request.BillRequestDto
import com.chagu.splitbill.dto.request.SettlementBillRequestDto
import com.chagu.splitbill.dto.response.BillResponseDto
import com.chagu.splitbill.dto.response.SplitDetailsResponseDto
import com.chagu.splitbill.enum.ErrorMessages
import com.chagu.splitbill.exceptions.BillNotFoundException
import com.chagu.splitbill.exceptions.InvalidBillException
import com.chagu.splitbill.model.BillEntity
import com.chagu.splitbill.model.GroupEntity
import com.chagu.splitbill.model.SplitDetailsEntity
import com.chagu.splitbill.repository.BillRepository
import com.chagu.splitbill.repository.GroupRepository
import com.chagu.splitbill.repository.SplitDetailsRepository
import com.chagu.splitbill.repository.UserRepository
import com.chagu.splitbill.utils.DateTimeUtil
import com.chagu.splitbill.utils.LoggerProvider
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BillService(
    private val billRepository: BillRepository, private val splitDetailsRepository: SplitDetailsRepository,
    private val groupRepository: GroupRepository, private val userRepository: UserRepository
) {

    companion object {
        val LOG: Logger = LoggerProvider.getLogger(BillService::class.java)
    }

    @Transactional(readOnly = true)
    fun findBillByBillId(billId: Int): BillResponseDto {
        LOG.info("Finding Bill with bill_id : $billId")
        var entity: Optional<BillEntity> = billRepository.findById(billId)
        if (!entity.isPresent || !entity.get().isActive)
            throw BillNotFoundException("${ErrorMessages.NO_RECORD_FOUND.message} with bill_Id : $billId")
        return mapBillEntityToBillResponseDto(entity.get())
    }

    @Transactional
    fun saveNewBill(billRequestDto: BillRequestDto): BillResponseDto {
        LOG.info("Saving new Bill against : ${billRequestDto.billAgainst}")
        var entity: BillEntity = mapBillBillRequestDtoToBillEntity(billRequestDto)
        var savedEntity: BillEntity = billRepository.save(entity)
        return mapBillEntityToBillResponseDto(savedEntity)
    }

    fun deleteBill(billId: Int) {
        LOG.info("Deleting Bill with bill_id : $billId")
        var entity: Optional<BillEntity> = billRepository.findById(billId)
        if (!entity.isPresent)
            throw BillNotFoundException("${ErrorMessages.NO_RECORD_FOUND.message} with bill_Id : $billId")
        var savedEntity: BillEntity = entity.get()
        savedEntity.isActive = false
        billRepository.save(savedEntity)
    }

    @Transactional
    fun undoDeletedBill(billId: Int): BillResponseDto {
        LOG.info("Processing Undo of a deleted Bill with bill_id : $billId")
        var entity: Optional<BillEntity> = billRepository.findById(billId)
        if (!entity.isPresent)
            throw BillNotFoundException("${ErrorMessages.NO_RECORD_FOUND.message} with bill_Id : $billId")
        var savedEntity: BillEntity = entity.get()
        savedEntity.isActive = true
        var undoneBill = billRepository.save(savedEntity)
        return mapBillEntityToBillResponseDto(undoneBill)
    }

    @Transactional
    fun settleBill(billRequestDto: SettlementBillRequestDto): BillResponseDto {
        LOG.info("Settling Bill of bill_id: ${billRequestDto.billId}")
        var entity: Optional<BillEntity> = billRepository.findById(billRequestDto.billId)
        if (!entity.isPresent)
            throw BillNotFoundException("${ErrorMessages.NO_RECORD_FOUND.message} with bill_Id : ${billRequestDto.billId}")
        var savedEntity: BillEntity = entity.get()
        savedEntity.isSettled = billRequestDto.isFullySettled

        for (split in billRequestDto.splitDetails) {
            var splitEntity: SplitDetailsEntity = splitDetailsRepository
                .findSplitDetailsForSettlement(billRequestDto.billId, split.paidToUserId, split.paidByUserId)
            if (splitEntity != null) {
                splitEntity.amountPaid = splitEntity.amountPaid?.minus(split.amountPaid)
                splitEntity.isSettled = split.isSettled
                splitDetailsRepository.save(splitEntity)
            }
        }

        var updatedEntity: BillEntity = billRepository.save(savedEntity)
        return mapBillEntityToBillResponseDto(updatedEntity)
    }

    private fun mapBillEntityToBillResponseDto(entity: BillEntity): BillResponseDto {
        var responseDto = BillResponseDto()
        responseDto.billId = entity.id
        responseDto.billAgainst = entity.billAgainst
        responseDto.groupId = entity.group?.id
        responseDto.groupName = entity.group?.groupName
        responseDto.totalAmount = entity.totalAmount
        responseDto.totalAmountPerHead = entity.amountPerHead
        responseDto.billDateTime = entity.billDateTime
        responseDto.isSettled = entity.isSettled

        var splitDetailsList = mutableListOf<SplitDetailsResponseDto>()
        for (splitEntity in entity.splitDetails) {
            var splitDto = SplitDetailsResponseDto()
            splitDto.billId = entity.id
            splitDto.paidByUserId = splitEntity.paidBy?.id
            splitDto.paidByUserName = "${splitEntity.paidBy?.firstName} ${splitEntity.paidBy?.lastName}"
            splitDto.paidToUserId = splitEntity.paidTo?.id
            splitDto.paidToUserName = "${splitEntity.paidTo?.firstName} ${splitEntity.paidTo?.lastName}"
            splitDto.paidAmount = splitEntity.amountPaid
            splitDetailsList.add(splitDto)
        }
        responseDto.splitDetails = splitDetailsList

        return responseDto
    }

    private fun mapBillBillRequestDtoToBillEntity(billRequestDto: BillRequestDto): BillEntity {
        if (billRequestDto.billAgainst.isEmpty() || billRequestDto.totalAmount < 1)
            throw InvalidBillException(ErrorMessages.MISSING_REQUIRED_FIELD.message)

        var groupEntity: Optional<GroupEntity> = groupRepository.findById(billRequestDto.groupId)
        if (!groupEntity.isPresent)
            throw InvalidBillException("${ErrorMessages.NO_RECORD_FOUND.message} with groupId : $billRequestDto.groupId")
        var billEntity = BillEntity()
        billEntity.billAgainst = billRequestDto.billAgainst
        billEntity.group = groupEntity.get()
        billEntity.billDateTime = DateTimeUtil.getLocalDateTimeFromString(billRequestDto.billDateTime)
        billEntity.totalAmount = billRequestDto.totalAmount
        billEntity.amountPerHead = billRequestDto.totalAmount / groupEntity.get().users.size
        /* FIXME: All Users_Ids need to be validated by group provided*/
        var splitDetailsList = mutableListOf<SplitDetailsEntity>()
        for (splitDetailsDto in billRequestDto.splitDetails) {
            var splitEntity = SplitDetailsEntity()
            splitEntity.billEntity = billEntity
            splitEntity.paidBy = userRepository.getOne(splitDetailsDto.paidByUserId)
            splitEntity.paidTo = userRepository.getOne(splitDetailsDto.paidToUserId)
            splitEntity.amountPaid = splitDetailsDto.amountPaid
            splitEntity.isSettled = false
            splitDetailsList.add(splitEntity)
        }
        billEntity.splitDetails = splitDetailsList
        billEntity.isSettled = false

        return billEntity
    }

}