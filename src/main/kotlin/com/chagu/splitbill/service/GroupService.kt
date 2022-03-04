package com.chagu.splitbill.service

import com.chagu.splitbill.dto.request.GroupRequestDto
import com.chagu.splitbill.dto.response.GroupResponseDto
import com.chagu.splitbill.enum.ErrorMessages
import com.chagu.splitbill.exceptions.GroupCreationException
import com.chagu.splitbill.exceptions.GroupNotFoundException
import com.chagu.splitbill.model.GroupEntity
import com.chagu.splitbill.model.UserEntity
import com.chagu.splitbill.repository.GroupRepository
import com.chagu.splitbill.repository.UserRepository
import com.chagu.splitbill.utils.GroupUtils
import com.chagu.splitbill.utils.LoggerProvider
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class GroupService(private val groupRepository: GroupRepository, private val userRepository: UserRepository) {

    companion object {
        val LOG: Logger = LoggerProvider.getLogger(GroupService::class.java)
    }

    fun saveNewGroup(groupRequest: GroupRequestDto): GroupResponseDto {
        LOG.info("Saving new Group with ${groupRequest.groupMembersId.size} number of users !!!")
        if (groupRequest.groupMembersId.isNotEmpty() && groupRequest.groupName.isNotBlank()) {
            var savedGroup: GroupEntity = groupRepository.save(mapGroupRequestToGroupEntity(groupRequest))
            return GroupUtils.mapGroupEntityToGroupResponseDto(savedGroup)
        } else {
            throw GroupCreationException(ErrorMessages.MISSING_REQUIRED_FIELD.message)
        }
    }

    @Transactional(readOnly = true)
    fun getGroupByGroupId(groupId: Int): GroupResponseDto {
        LOG.info("Finding new Group with group_id : $groupId !!!")
        var groupEntity: Optional<GroupEntity> = groupRepository.findById(groupId)
        if (groupEntity.isPresent) {
            return GroupUtils.mapGroupEntityToGroupResponseDto(groupEntity.get())
        } else {
            throw GroupNotFoundException("${ErrorMessages.NO_RECORD_FOUND.message} with groupId : $groupId")
        }
    }

    private fun mapGroupRequestToGroupEntity(groupRequest: GroupRequestDto): GroupEntity {
        var userEntities: List<UserEntity> = userRepository.findAllById(groupRequest.groupMembersId)
        var newGroup: GroupEntity = GroupEntity()
        newGroup.groupName = groupRequest.groupName
        newGroup.users = userEntities
        return newGroup
    }

}