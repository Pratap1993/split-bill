package com.chagu.splitbill.config

import com.chagu.splitbill.dto.request.BillRequestDto
import com.chagu.splitbill.dto.request.GroupRequestDto
import com.chagu.splitbill.dto.request.SplitDetailsRequestDto
import com.chagu.splitbill.dto.request.UserRequestDto
import com.chagu.splitbill.service.BillService
import com.chagu.splitbill.service.GroupService
import com.chagu.splitbill.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DatabaseLoader(
    private val userService: UserService, private val groupService: GroupService,
    private val billService: BillService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        saveUsers()
        var groupId: Int? = saveGroup()
        saveBill(groupId)
        anotherGroupWithBill()
    }

    private fun saveUsers() {
        var user1 = UserRequestDto(
            "Pratap Bhanu", "Dhal", "pratap.dhal@chagu.com",
            "9113516428"
        )
        var user2 = UserRequestDto(
            "Sachin Ramesh", "Tendulkar", "sachin.rt@chagu.com",
            "9113516222"
        )
        var user3 = UserRequestDto(
            "Rohit", "Sharma", "rohit.45@chagu.com",
            "9143436428"
        )
        var user4 = UserRequestDto(
            "Yuvraaj", "Singh", "yuvi.strong@chagu.com",
            "9113313428"
        )

        userService.saveNewUser(user1)
        userService.saveNewUser(user2)
        userService.saveNewUser(user3)
        userService.saveNewUser(user4)
    }

    private fun saveGroup(): Int? {
        var group = GroupRequestDto("Cricket Gurus", mutableListOf(1, 2, 3, 4))
        var groupResponse = groupService.saveNewGroup(group)
        return groupResponse.id
    }

    private fun saveBill(groupId: Int?) {
        var groupEntityId = groupId ?: 1

        var split1 = SplitDetailsRequestDto(1, 4, 2000, false)
        var split2 = SplitDetailsRequestDto(2, 3, 1000, false)
        var splitDetailsList = mutableListOf<SplitDetailsRequestDto>(split1, split2)
        var billRequestDto =
            BillRequestDto("Lunch at 'The Black Pearl'", groupEntityId, 8000, "2020-05-22 13:00", splitDetailsList)
        billService.saveNewBill(billRequestDto)
    }

    private fun anotherGroupWithBill() {
        var group = GroupRequestDto("Foodie Group", mutableListOf(2, 4))
        var groupResponse = groupService.saveNewGroup(group)

        var groupId: Int = groupResponse.id ?: 6

        var split = SplitDetailsRequestDto(2, 4, 1000, false)
        var splitDetailsList = mutableListOf(split)
        var billRequestDto =
            BillRequestDto("Lunch at 'Hotel Palace'", groupId, 3000, "2020-05-11 13:00", splitDetailsList)
        billService.saveNewBill(billRequestDto)
    }

}