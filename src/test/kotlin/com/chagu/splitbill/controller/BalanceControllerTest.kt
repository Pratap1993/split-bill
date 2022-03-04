package com.chagu.splitbill.controller

import com.chagu.splitbill.service.BalanceService
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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@WebMvcTest(BalanceController::class)
internal class BalanceControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var balanceService: BalanceService

    @Test
    fun `test get BalanceResponse`() {
        Mockito.`when`(balanceService.getBalanceByUserId(2))
            .thenReturn(DummyDataProvider.getDummyBalanceResponseDto())
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("/api/balance/2")
            .characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        assertEquals(HttpStatus.OK.value(), result.response.status)
        val returnExpected: String = ObjectMapper().writeValueAsString(DummyDataProvider.getDummyBalanceResponseDto())
        JSONAssert.assertEquals(returnExpected, result.response.contentAsString, false)
    }

    @Test
    fun `test get individual BalanceResponse`() {
        Mockito.`when`(balanceService.getIndividualBalance(1, 4))
            .thenReturn(DummyDataProvider.getDummyIndividualBalanceResponseDto())
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("/api/balance/1/4")
            .characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        assertEquals(HttpStatus.OK.value(), result.response.status)
        val returnExpected: String =
            ObjectMapper().writeValueAsString(DummyDataProvider.getDummyIndividualBalanceResponseDto())
        JSONAssert.assertEquals(returnExpected, result.response.contentAsString, false)
    }
}