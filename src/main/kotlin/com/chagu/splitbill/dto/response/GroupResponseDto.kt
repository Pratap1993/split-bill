package com.chagu.splitbill.dto.response

class GroupResponseDto {

    var id: Int? = null

    var groupName: String? = null

    var users = mutableListOf<UserResponseDto>()
}