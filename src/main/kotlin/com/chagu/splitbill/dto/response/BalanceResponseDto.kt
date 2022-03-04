package com.chagu.splitbill.dto.response

class BalanceResponseDto {

    var userName: String? = null

    var email: String? = null

    var owesTo: List<UserOwesDto>? = null

    var otherUserOwes: List<OtherOwesDto>? = null

    var totalAmountToGive: Long? = null

    var totalAmountToGet: Long? = null
}