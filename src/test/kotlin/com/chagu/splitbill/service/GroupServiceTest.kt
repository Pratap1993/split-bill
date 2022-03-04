package com.chagu.splitbill.service

import com.chagu.splitbill.model.GroupEntity
import com.chagu.splitbill.repository.GroupRepository
import com.chagu.splitbill.repository.UserRepository
import com.chagu.splitbill.utils.DummyDataProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

internal class GroupServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var groupRepository: GroupRepository

    private lateinit var groupService: GroupService

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
        groupService = GroupService(groupRepository, userRepository)
    }

    @Test
    fun `test saving new Group`() {
        Mockito.`when`(groupRepository.save(Mockito.any(GroupEntity::class.java)))
            .thenReturn(DummyDataProvider.getDummyGroupEntity())
        Mockito.`when`(userRepository.findAllById(Mockito.anyCollection()))
            .thenReturn(DummyDataProvider.getDummyUserEntityList())
        val response = groupService.saveNewGroup(DummyDataProvider.getDummyGroupRequestDto())
        assertEquals(response.groupName, DummyDataProvider.getDummyGroupEntity().groupName)
        assertEquals(response.users.size, DummyDataProvider.getDummyGroupEntity().users.size)
    }
}