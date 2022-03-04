package com.chagu.splitbill.utils

import com.chagu.splitbill.dto.request.UserRequestDto
import com.chagu.splitbill.dto.response.UserResponseDto
import com.chagu.splitbill.model.UserEntity

class UserUtils {
    companion object {
        fun mapUserRequestToUserEntity(userRequest: UserRequestDto): UserEntity {
            var newUser = UserEntity()
            newUser.firstName = userRequest.firstName
            newUser.lastName = userRequest.lastName
            newUser.email = userRequest.email
            newUser.mobile = userRequest.mobile
            newUser.isActive = true
            return newUser
        }

        fun mapUserEntityToUserResponseDto(userEntity: UserEntity): UserResponseDto {
            var dto = UserResponseDto()
            dto.id = userEntity.id
            dto.fullName = "${userEntity.firstName} ${userEntity.lastName}"
            dto.email = userEntity.email
            dto.mobile = userEntity.mobile
            return dto
        }
    }
}