package com.chagu.splitbill.dto.request

// Bill DateTime should be in "2020-04-13 18:00" format
data class BillRequestDto(
    var billAgainst: String,
    var groupId: Int,
    var totalAmount: Long,
    var billDateTime: String,
    var splitDetails: List<SplitDetailsRequestDto>
)

