package com.chagu.splitbill.controller

import com.chagu.splitbill.enum.ErrorMessages
import com.chagu.splitbill.exceptions.UserServiceException
import com.chagu.splitbill.service.UserService
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

@WebMvcTest(UsersController::class)
internal class UsersControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `test find one user with existing userId`() {
        Mockito.`when`(userService.findUserById(22)).thenReturn(DummyDataProvider.getDummyUserResponseDto())
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("/api/users/22")
            .characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        assertEquals(HttpStatus.OK.value(), result.response.status)
        val returnExpected: String = ObjectMapper().writeValueAsString(DummyDataProvider.getDummyUserResponseDto())
        JSONAssert.assertEquals(returnExpected, result.response.contentAsString, false)
    }

    @Test
    fun `test find one user with not existing userId`() {
        Mockito.`when`(userService.findUserById(440))
            .thenThrow(UserServiceException("${ErrorMessages.NO_RECORD_FOUND.message} with userID : 440"))
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("/api/users/440")
            .characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        assertEquals(HttpStatus.NOT_FOUND.value(), result.response.status)
    }

    @Test
    fun `test saving new user`() {
        Mockito.`when`(userService.saveNewUser(DummyDataProvider.getDummyUserRequestDto()))
            .thenReturn(DummyDataProvider.getDummyUserResponseDto())
        var mapper: ObjectMapper = ObjectMapper()
        var payload: String = mapper.writeValueAsString(DummyDataProvider.getDummyUserRequestDto())
        var requestBuilder: RequestBuilder = MockMvcRequestBuilders.post("/api/users/")
            .accept(MediaType.APPLICATION_JSON).content(payload).contentType(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        val response: MockHttpServletResponse = result.response
        assertEquals(HttpStatus.CREATED.value(), response.status)
        var returnExpected: String = mapper.writeValueAsString(DummyDataProvider.getDummyUserResponseDto())
        JSONAssert.assertEquals(returnExpected, result.response.contentAsString, false)
    }

    @Test
    fun `test updating an existing record`() {
        Mockito.`when`(userService.updateUser(1, DummyDataProvider.getDummyUserRequestDto()))
            .thenReturn(DummyDataProvider.getDummyUserResponseDto())
        var mapper: ObjectMapper = ObjectMapper()
        var payload: String = mapper.writeValueAsString(DummyDataProvider.getDummyUserRequestDto())
        var requestBuilder: RequestBuilder = MockMvcRequestBuilders.put("/api/users/1")
            .accept(MediaType.APPLICATION_JSON).content(payload).contentType(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        val response: MockHttpServletResponse = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        var returnExpected: String = mapper.writeValueAsString(DummyDataProvider.getDummyUserResponseDto())
        JSONAssert.assertEquals(returnExpected, result.response.contentAsString, false)
    }
}