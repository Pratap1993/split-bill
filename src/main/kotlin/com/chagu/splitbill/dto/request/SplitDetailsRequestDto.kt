package com.chagu.splitbill.dto.request

data class SplitDetailsRequestDto(
    var paidByUserId: Int,
    var paidToUserId: Int,
    var amountPaid: Long,
    var isSettled: Boolean = false
)