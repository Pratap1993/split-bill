package com.chagu.splitbill.service

import com.chagu.splitbill.dto.request.UserRequestDto
import com.chagu.splitbill.exceptions.UserServiceException
import com.chagu.splitbill.model.UserEntity
import com.chagu.splitbill.repository.UserRepository
import com.chagu.splitbill.utils.DummyDataProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

internal class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var userService: UserService

    private lateinit var newUser: UserRequestDto

    private lateinit var existingUser: UserRequestDto

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
        userService = UserService(userRepository)
        newUser = UserRequestDto("Sachin", "Tendulkar", "sachin.123@gmail.com", "9937125281")
        existingUser = UserRequestDto("Pratap Bhanu", "Dhal", "chagusss@gmail.com", "9113516428")
    }

    @Test
    fun `test creating new user`() {
        Mockito.`when`(userRepository.save(Mockito.any(UserEntity::class.java)))
            .thenReturn(DummyDataProvider.getDummyUserEntity())
        val response = userService.saveNewUser(newUser)
        assertEquals(response.fullName, "Pratap Bhanu Dhal")
        assertEquals(response.id, 1)
    }

    @Test
    fun `test creating new user with existing email`() {
        Mockito.`when`(userRepository.findByEmail("chagusss@gmail.com"))
            .thenReturn(DummyDataProvider.getDummyUserEntity())
        assertThrows<UserServiceException> { userService.saveNewUser(existingUser) }
    }

    @Test
    fun `test find User with userID`() {
        Mockito.`when`(userRepository.findById(1)).thenReturn(Optional.of(DummyDataProvider.getDummyUserEntity()))
        val response = userService.findUserById(1)
        assertEquals(response.fullName, "Pratap Bhanu Dhal")
        assertEquals(response.email, "chagusss@gmail.com")
    }
}