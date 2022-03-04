package com.chagu.splitbill.dto.request

data class SettlementBillRequestDto(
    var billId: Int,
    var isFullySettled: Boolean,
    var splitDetails: List<SplitDetailsRequestDto>
)