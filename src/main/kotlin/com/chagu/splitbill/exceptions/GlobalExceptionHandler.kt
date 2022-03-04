package com.chagu.splitbill.exceptions

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.util.*


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [(UserServiceException::class)])
    fun userServiceException(ex: UserServiceException, request: WebRequest): ResponseEntity<CustomExceptionBean> {
        val errorMessage = CustomExceptionBean(Date(), ex.exceptionMessage)
        return ResponseEntity(errorMessage, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(GroupCreationException::class)])
    fun handleGroupCreationException(
        ex: GroupCreationException,
        request: WebRequest
    ): ResponseEntity<CustomExceptionBean> {
        val errorMessage = CustomExceptionBean(Date(), ex.exceptionMessage)
        return ResponseEntity(errorMessage, HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(GroupNotFoundException::class)])
    fun handleGroupNotFoundException(
        ex: GroupNotFoundException,
        request: WebRequest
    ): ResponseEntity<CustomExceptionBean> {
        val errorMessage = CustomExceptionBean(Date(), ex.exceptionMessage)
        return ResponseEntity(errorMessage, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(InvalidBillException::class)])
    fun handleInvalidBillException(ex: InvalidBillException, request: WebRequest): ResponseEntity<CustomExceptionBean> {
        val errorMessage = CustomExceptionBean(Date(), ex.exceptionMessage)
        return ResponseEntity(errorMessage, HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(BillNotFoundException::class)])
    fun handleBillNotFoundException(
        ex: BillNotFoundException,
        request: WebRequest
    ): ResponseEntity<CustomExceptionBean> {
        val errorMessage = CustomExceptionBean(Date(), ex.exceptionMessage)
        return ResponseEntity(errorMessage, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

}