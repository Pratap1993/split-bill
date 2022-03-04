package com.chagu.splitbill.controller

import com.chagu.splitbill.dto.request.BillRequestDto
import com.chagu.splitbill.dto.response.BillResponseDto
import com.chagu.splitbill.enum.ErrorMessages
import com.chagu.splitbill.exceptions.BillNotFoundException
import com.chagu.splitbill.service.BillService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
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

@WebMvcTest(BillController::class)
internal class BillControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var billService: BillService

    @Test
    fun `test find Bill with existing bill_id`() {
        var dummyResponse = BillResponseDto()
        dummyResponse.billAgainst = "Testing Bill"
        Mockito.`when`(billService.findBillByBillId(2)).thenReturn(dummyResponse)
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("/api/bills/2")
            .characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        assertEquals(HttpStatus.OK.value(), result.response.status)
    }

    @Test
    fun `test find Bill with not existing bill_id`() {
        Mockito.`when`(billService.findBillByBillId(20))
            .thenThrow(BillNotFoundException("${ErrorMessages.NO_RECORD_FOUND.message} with BillId : 20"))
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("/api/bills/20")
            .characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        assertEquals(HttpStatus.NOT_FOUND.value(), result.response.status)
    }

    @Test
    fun `test saving new Bill`() {
        var billRequest = BillRequestDto(
            "Testing Bill", 1, 200,
            "2020-04-13 18:00", mutableListOf()
        )
        var billResponse = BillResponseDto()
        billResponse.billAgainst = "Testing Bill"
        Mockito.`when`(billService.saveNewBill(billRequest)).thenReturn(billResponse)
        var mapper = ObjectMapper()
        var payload: String = mapper.writeValueAsString(billRequest)
        var requestBuilder: RequestBuilder = MockMvcRequestBuilders.post("/api/bills/")
            .accept(MediaType.APPLICATION_JSON).content(payload).contentType(MediaType.APPLICATION_JSON)
        var result: MvcResult = mockMvc.perform(requestBuilder).andReturn()
        val response: MockHttpServletResponse = result.response
        assertEquals(HttpStatus.CREATED.value(), response.status)
    }
}