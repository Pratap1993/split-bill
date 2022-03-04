package com.chagu.splitbill.controller

import com.chagu.splitbill.enum.ErrorMessages
import com.chagu.splitbill.exceptions.GroupNotFoundException
import com.chagu.splitbill.service.GroupService
import com.chagu.splitbill.utils.DummyDataProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@WebMvcTest(GroupController::class)
internal class GroupControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var groupService: GroupService

    @Test
    fun `test find one group with existing groupId`() {
        Mockito.`when`(groupService.getGroupByGroupId(20))
            .thenReturn(DummyDataProvider.getDummyGroupResponseDto())
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("/api/groups/20")
            .characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        assertEquals(HttpStatus.OK.value(), result.response.status)
        val returnExpected: String = ObjectMapper().writeValueAsString(DummyDataProvider.getDummyGroupResponseDto())
        JSONAssert.assertEquals(returnExpected, result.response.contentAsString, false)
    }

    @Test
    fun `test find one group with invalid groupId`() {
        Mockito.`when`(groupService.getGroupByGroupId(440))
            .thenThrow(GroupNotFoundException("${ErrorMessages.NO_RECORD_FOUND.message} with groupId : 440"))
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("/api/groups/440")
            .characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        assertEquals(HttpStatus.NOT_FOUND.value(), result.response.status)
    }

    @Test
    fun `test saving new group`() {
        Mockito.`when`(groupService.saveNewGroup(DummyDataProvider.getDummyGroupRequestDto()))
            .thenReturn(DummyDataProvider.getDummyGroupResponseDto())
        var mapper: ObjectMapper = ObjectMapper()
        var payload: String = mapper.writeValueAsString(DummyDataProvider.getDummyGroupRequestDto())
        var requestBuilder: RequestBuilder = MockMvcRequestBuilders.post("/api/groups/")
            .accept(MediaType.APPLICATION_JSON).content(payload).contentType(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        val response: MockHttpServletResponse = result.response
        assertEquals(HttpStatus.CREATED.value(), response.status)
        var returnExpected: String = mapper.writeValueAsString(DummyDataProvider.getDummyGroupResponseDto())
        JSONAssert.assertEquals(returnExpected, result.response.contentAsString, false)
    }
}