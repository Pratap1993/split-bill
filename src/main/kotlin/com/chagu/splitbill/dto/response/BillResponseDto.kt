package com.chagu.splitbill.dto.response

import java.time.LocalDateTime

class BillResponseDto {

    var billId: Int? = null

    var billAgainst: String? = null

    var groupId: Int? = null

    var groupName: String? = null

    var totalAmount: Long? = null

    var totalAmountPerHead: Long? = null

    var billDateTime: LocalDateTime? = null

    var isSettled: Boolean? = null

    var splitDetails: MutableList<SplitDetailsResponseDto>? = null

}