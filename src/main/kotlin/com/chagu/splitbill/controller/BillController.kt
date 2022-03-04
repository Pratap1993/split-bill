package com.chagu.splitbill.controller

import com.chagu.splitbill.dto.request.BillRequestDto
import com.chagu.splitbill.dto.request.SettlementBillRequestDto
import com.chagu.splitbill.dto.response.BillResponseDto
import com.chagu.splitbill.service.BillService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/bills")
class BillController(private val billService: BillService) {

    @GetMapping("/{billId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findOne(@PathVariable billId: Int) =
        billService.findBillByBillId(billId)

    @PostMapping(
        "/", consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun saveNewBill(@Valid @RequestBody newBill: BillRequestDto): ResponseEntity<BillResponseDto> {
        return ResponseEntity(billService.saveNewBill(newBill), HttpHeaders(), HttpStatus.CREATED)
    }

    @DeleteMapping("/{billId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteBillById(@PathVariable billId: Int): ResponseEntity<String> {
        billService.deleteBill(billId)
        return ResponseEntity("Bill with bill_id : $billId deleted successfully !!!", HttpHeaders(), HttpStatus.OK)
    }

    @GetMapping("/undoDeletedBill/{billId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun unDoDeletedBill(@PathVariable billId: Int): ResponseEntity<BillResponseDto> {
        return ResponseEntity(billService.undoDeletedBill(billId), HttpHeaders(), HttpStatus.OK)
    }

    @PutMapping(
        "/settleBill", consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun settleBill(@Valid @RequestBody bill: SettlementBillRequestDto): ResponseEntity<BillResponseDto> {
        return ResponseEntity(billService.settleBill(bill), HttpHeaders(), HttpStatus.OK)
    }

}