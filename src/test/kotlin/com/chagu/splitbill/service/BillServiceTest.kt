package com.chagu.splitbill.service

import com.chagu.splitbill.dto.request.BillRequestDto
import com.chagu.splitbill.model.BillEntity
import com.chagu.splitbill.model.GroupEntity
import com.chagu.splitbill.repository.BillRepository
import com.chagu.splitbill.repository.GroupRepository
import com.chagu.splitbill.repository.SplitDetailsRepository
import com.chagu.splitbill.repository.UserRepository
import com.chagu.splitbill.utils.DummyDataProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime
import java.util.*

internal class BillServiceTest {

    @Mock
    private lateinit var billRepository: BillRepository

    @Mock
    private lateinit var groupRepository: GroupRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var splitDetailsRepository: SplitDetailsRepository

    private lateinit var billService: BillService

    private lateinit var billRequest: BillRequestDto

    private lateinit var billEntity: BillEntity

    private lateinit var deletedBillEntity: BillEntity

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
        billService = BillService(billRepository, splitDetailsRepository, groupRepository, userRepository)
        var dummyRequest = BillRequestDto(
            "Testing Bill", 1,
            200, "2020-04-13 18:00", mutableListOf()
        )
        billRequest = dummyRequest

        billEntity = BillEntity()
        billEntity.splitDetails = mutableListOf()
        billEntity.id = 2
        billEntity.totalAmount = 200
        billEntity.amountPerHead = 50
        billEntity.billDateTime = LocalDateTime.now()
        billEntity.group = GroupEntity()
        billEntity.billAgainst = "Testing Bill"
        billEntity.isSettled = false
        billEntity.isActive = true

        deletedBillEntity = billEntity
    }

    @Test
    fun `test find bill by bill_Id`() {
        Mockito.`when`(billRepository.findById(1))
            .thenReturn(Optional.of(billEntity))
        val response = billService.findBillByBillId(1)
        assertEquals(response.billAgainst, billEntity.billAgainst)
        assertEquals(response.totalAmount, 200)
    }

    @Test
    fun `test saving new Bill`() {
        Mockito.`when`(billRepository.save(Mockito.any(BillEntity::class.java)))
            .thenReturn(billEntity)
        Mockito.`when`(groupRepository.findById(Mockito.anyInt()))
            .thenReturn(Optional.of(DummyDataProvider.getDummyGroupEntity()))
        Mockito.`when`(userRepository.getOne(Mockito.anyInt()))
            .thenReturn(DummyDataProvider.getDummyUserEntity())
        val response = billService.saveNewBill(billRequest)
        assertEquals(response.billAgainst, billEntity.billAgainst)
        assertEquals(response.totalAmount, 200)
    }

    @Test
    fun `test undo deleted Bill`() {
        Mockito.`when`(billRepository.findById(Mockito.anyInt()))
            .thenReturn(Optional.of(billEntity))
        Mockito.`when`(billRepository.save(Mockito.any(BillEntity::class.java)))
            .thenReturn(billEntity)
        val response = billService.undoDeletedBill(2)
        assertEquals(response.totalAmount, billEntity.totalAmount)
    }

    @Test
    fun `test bill settlement`() {
        Mockito.`when`(billRepository.findById(Mockito.anyInt()))
            .thenReturn(Optional.of(deletedBillEntity))
        Mockito.`when`(billRepository.save(Mockito.any(BillEntity::class.java)))
            .thenReturn(DummyDataProvider.getDummyBillEntityList().first())
        val response = billService.settleBill(DummyDataProvider.getDummySettlementBillRequestDto())
        assertEquals(response.totalAmount, 2000)
    }
}