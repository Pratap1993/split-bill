package com.chagu.splitbill.utils

import com.chagu.splitbill.dto.response.GroupResponseDto
import com.chagu.splitbill.model.GroupEntity

class GroupUtils {
    companion object {
        fun mapGroupEntityToGroupResponseDto(groupEntity: GroupEntity): GroupResponseDto {
            var dto = GroupResponseDto()
            dto.id = groupEntity.id
            dto.groupName = groupEntity.groupName
            for (userEntity in groupEntity.users) {
                dto.users.add(UserUtils.mapUserEntityToUserResponseDto(userEntity))
            }
            return dto
        }
    }
}