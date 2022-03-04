package com.chagu.splitbill.service

import com.chagu.splitbill.repository.BillRepository
import com.chagu.splitbill.utils.DummyDataProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

internal class BalanceServiceTest {

    @Mock
    private lateinit var billRepository: BillRepository

    @Mock
    private lateinit var userService: UserService

    private lateinit var balanceService: BalanceService


    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
        balanceService = BalanceService(billRepository, userService)
    }

    @Test
    fun `test get balance by user_id`() {
        Mockito.`when`(userService.findUserById(Mockito.anyInt()))
            .thenReturn(DummyDataProvider.getDummyUserResponseDto())
        Mockito.`when`(billRepository.findListOfBillsForBalance(Mockito.anyInt()))
            .thenReturn(DummyDataProvider.getDummyBillEntityList())
        val response = balanceService.getBalanceByUserId(4)
        assertEquals(response.totalAmountToGet, 0)
        assertEquals(response.totalAmountToGive, 500)
    }

    @Test
    fun `test get individual balance by user_id and searched_user_id`() {
        Mockito.`when`(userService.findUserById(2))
            .thenReturn(DummyDataProvider.getDummyUserResponseDto())
        Mockito.`when`(userService.findUserById(4))
            .thenReturn(DummyDataProvider.getAnotherDummyUserResponseDto())
        Mockito.`when`(billRepository.findListOfBillsForIndividualBalance(2, 4))
            .thenReturn(DummyDataProvider.getDummyBillEntityList())
        val response = balanceService.getIndividualBalance(2, 4)
        assertEquals(response.userName, "Pratap Bhanu Dhal")
        assertEquals(response.searchedUserName, "Yuvraaj Singh")
        assertEquals(response.totalAmountToGet, 200)
        assertEquals(response.totalAmountToGive, 300)
    }
}