package com.chagu.splitbill.controller

import com.chagu.splitbill.dto.request.UserRequestDto
import com.chagu.splitbill.dto.response.UserResponseDto
import com.chagu.splitbill.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UsersController(private val userService: UserService) {

    @GetMapping("/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findOne(@PathVariable userId: Int) =
        userService.findUserById(userId)

    @PostMapping(
        "/", consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun saveNewUser(@Valid @RequestBody newUser: UserRequestDto): ResponseEntity<UserResponseDto> {
        return ResponseEntity(userService.saveNewUser(newUser), HttpHeaders(), HttpStatus.CREATED)
    }

    @PutMapping(
        "/{userId}", consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateUser(@PathVariable userId: Int, @Valid @RequestBody userToUpdate: UserRequestDto): UserResponseDto {
        return userService.updateUser(userId, userToUpdate)
    }
}