package com.chagu.splitbill.utils

import com.chagu.splitbill.dto.request.GroupRequestDto
import com.chagu.splitbill.dto.request.SettlementBillRequestDto
import com.chagu.splitbill.dto.request.SplitDetailsRequestDto
import com.chagu.splitbill.dto.request.UserRequestDto
import com.chagu.splitbill.dto.response.BalanceResponseDto
import com.chagu.splitbill.dto.response.GroupResponseDto
import com.chagu.splitbill.dto.response.IndividualBalanceResponseDto
import com.chagu.splitbill.dto.response.UserResponseDto
import com.chagu.splitbill.model.BillEntity
import com.chagu.splitbill.model.GroupEntity
import com.chagu.splitbill.model.SplitDetailsEntity
import com.chagu.splitbill.model.UserEntity
import java.time.LocalDateTime

object DummyDataProvider {

    fun getDummyUserEntity(): UserEntity {
        var dummy = UserEntity()
        dummy.id = 1
        dummy.firstName = "Pratap Bhanu"
        dummy.lastName = "Dhal"
        dummy.mobile = "9113516428"
        dummy.email = "chagusss@gmail.com"
        dummy.isActive = true
        return dummy
    }

    private fun getAnotherDummyUserEntity(): UserEntity {
        var dummy = UserEntity()
        dummy.id = 1
        dummy.firstName = "Yuvraaj"
        dummy.lastName = "Singh"
        dummy.mobile = "9113516428"
        dummy.email = "yuvi@gmail.com"
        dummy.isActive = true
        return dummy
    }

    fun getDummyUserEntityList(): MutableList<UserEntity> {
        var firstUser: UserEntity = getDummyUserEntity()

        var secondUser: UserEntity = UserEntity()
        secondUser.id = 2
        secondUser.firstName = "Chagulu"
        secondUser.lastName = "Dhal"
        secondUser.mobile = "9937125281"
        secondUser.email = "chagu123@gmail.com"
        secondUser.isActive = true

        var thirdUser: UserEntity = UserEntity()
        secondUser.id = 3
        secondUser.firstName = "Sachin"
        secondUser.lastName = "Tendulkar"
        secondUser.mobile = "9853472881"
        secondUser.email = "sachintheGod@gmail.com"
        secondUser.isActive = true

        return mutableListOf(firstUser, secondUser, thirdUser)
    }

    fun getDummyUserResponseDto(): UserResponseDto {
        var userEntity = getDummyUserEntity()
        var dto = UserResponseDto()
        dto.id = userEntity.id
        dto.fullName = "${userEntity.firstName} ${userEntity.lastName}"
        dto.email = userEntity.email
        dto.mobile = userEntity.mobile
        return dto
    }

    fun getAnotherDummyUserResponseDto(): UserResponseDto {
        var userEntity = getAnotherDummyUserEntity()
        var dto = UserResponseDto()
        dto.id = userEntity.id
        dto.fullName = "${userEntity.firstName} ${userEntity.lastName}"
        dto.email = userEntity.email
        dto.mobile = userEntity.mobile
        return dto
    }

    fun getDummyUserRequestDto(): UserRequestDto {
        return UserRequestDto(
            "Chagulu", "Dhal", "chagu@gmail.com",
            "9113516428"
        )
    }

    fun getDummyGroupEntity(): GroupEntity {
        var groupEntity: GroupEntity = GroupEntity()
        groupEntity.id = 1
        groupEntity.groupName = "Dummy Group For Testing"
        groupEntity.users = getDummyUserEntityList()
        return groupEntity
    }

    fun getDummyGroupRequestDto(): GroupRequestDto =
        GroupRequestDto("Dummy Group For Testing", mutableListOf(1, 2, 3))

    fun getDummyGroupResponseDto(): GroupResponseDto {
        return GroupUtils.mapGroupEntityToGroupResponseDto(getDummyGroupEntity())
    }

    fun getDummyBillEntityList(): List<BillEntity> {
        var user1 = UserEntity()
        user1.id = 2

        var user2 = UserEntity()
        user2.id = 3

        var user3 = UserEntity()
        user3.id = 4

        var group = GroupEntity()
        group.id = 5

        var bill1 = BillEntity()
        var sp1 = SplitDetailsEntity()
        sp1.paidBy = user1
        sp1.paidTo = user3
        sp1.amountPaid = 200

        var sp2 = SplitDetailsEntity()
        sp2.paidBy = user2
        sp2.paidTo = user3
        sp2.amountPaid = 300

        bill1.id = 1
        bill1.billAgainst = "Test Bill 1"
        bill1.isSettled = false
        bill1.group = group
        bill1.billDateTime = LocalDateTime.now()
        bill1.totalAmount = 2000
        bill1.amountPerHead = 500
        bill1.splitDetails = mutableListOf(sp1, sp2)

        return mutableListOf(bill1)
    }

    fun getDummyBalanceResponseDto(): BalanceResponseDto {
        var dto = BalanceResponseDto()
        dto.userName = "Chagulu Dhal"
        dto.totalAmountToGet = 50000
        dto.totalAmountToGive = 200
        dto.email = "chagusss@gmail.com"
        return dto
    }

    fun getDummyIndividualBalanceResponseDto(): IndividualBalanceResponseDto {
        var dto = IndividualBalanceResponseDto()
        dto.userId = 1
        dto.userName = "Pratap Bhanu"
        dto.searchedUserId = 4
        dto.searchedUserName = "Yuvraaj Singh"
        dto.totalAmountToGive = 0
        dto.totalAmountToGet = 2000
        return dto
    }

    fun getDummySettlementBillRequestDto(): SettlementBillRequestDto {
        var sp1 = SplitDetailsRequestDto(4, 2, 200, true)
        return SettlementBillRequestDto(2, false, mutableListOf(sp1))
    }

}