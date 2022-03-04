package com.chagu.splitbill.service

import com.chagu.splitbill.dto.request.UserRequestDto
import com.chagu.splitbill.dto.response.UserResponseDto
import com.chagu.splitbill.enum.ErrorMessages
import com.chagu.splitbill.exceptions.UserServiceException
import com.chagu.splitbill.model.UserEntity
import com.chagu.splitbill.repository.UserRepository
import com.chagu.splitbill.utils.LoggerProvider
import com.chagu.splitbill.utils.UserUtils
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    companion object {
        val LOG: Logger = LoggerProvider.getLogger(UserService::class.java)
    }

    fun saveNewUser(userRequest: UserRequestDto): UserResponseDto {
        LOG.info("Saving new User with email_id : ${userRequest.email}")
        var checkEmail = userRepository.findByEmail(userRequest.email)
        if (checkEmail != null)
            throw UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.message)
        var savedUser = userRepository.save(UserUtils.mapUserRequestToUserEntity(userRequest))
        return if (savedUser != null) UserUtils.mapUserEntityToUserResponseDto(savedUser) else
            throw UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.message)
    }

    fun findUserById(userId: Int): UserResponseDto {
        LOG.info("Finding User with user_id : $userId")
        var returnType: UserResponseDto
        var entity = userRepository.findById(userId)
        if (entity.isPresent)
            returnType = UserUtils.mapUserEntityToUserResponseDto(entity.get())
        else
            throw UserServiceException("${ErrorMessages.NO_RECORD_FOUND.message} with userID : $userId")
        return returnType
    }

    fun findUserByEmail(email: String): UserResponseDto {
        LOG.info("Finding User with email_id : $email")
        var entity: UserEntity? = userRepository.findByEmail(email)
        return if (entity != null) UserUtils.mapUserEntityToUserResponseDto(entity)
        else
            throw UserServiceException("${ErrorMessages.NO_RECORD_FOUND.message} with email : $email")
    }

    fun updateUser(userId: Int, userRequest: UserRequestDto): UserResponseDto {
        LOG.info("Updating User with user_id : $userId")
        var entity = userRepository.findById(userId)
        if (entity.isPresent) {
            var userToUpdate: UserEntity = entity.get()
            userToUpdate.email = userRequest.email
            userToUpdate.mobile = userRequest.mobile
            userToUpdate.firstName = userRequest.firstName
            userToUpdate.lastName = userRequest.lastName
            userToUpdate.isActive = true
            var updatedUSer: UserEntity = userRepository.save(userToUpdate)
            return UserUtils.mapUserEntityToUserResponseDto(updatedUSer)
        } else throw UserServiceException("${ErrorMessages.NO_RECORD_FOUND.message} with userID : $userId")
    }

}
