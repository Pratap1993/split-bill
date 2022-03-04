package com.chagu.splitbill.controller

import com.chagu.splitbill.dto.response.BalanceResponseDto
import com.chagu.splitbill.dto.response.IndividualBalanceResponseDto
import com.chagu.splitbill.service.BalanceService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/balance")
class BalanceController(private val balanceService: BalanceService) {

    @GetMapping("/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserBalance(@PathVariable userId: Int): ResponseEntity<BalanceResponseDto> {
        return ResponseEntity(balanceService.getBalanceByUserId(userId), HttpHeaders(), HttpStatus.OK)
    }

    @GetMapping("/{userId}/{searchUserId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getIndividualBalance(@PathVariable userId: Int, @PathVariable searchUserId: Int):
            ResponseEntity<IndividualBalanceResponseDto> {
        return ResponseEntity(balanceService.getIndividualBalance(userId, searchUserId), HttpHeaders(), HttpStatus.OK)
    }
}