package com.chagu.splitbill.controller

import com.chagu.splitbill.dto.request.GroupRequestDto
import com.chagu.splitbill.dto.response.GroupResponseDto
import com.chagu.splitbill.service.GroupService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/groups")
class GroupController(private val groupService: GroupService) {

    @GetMapping("/{groupId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findOne(@PathVariable groupId: Int) =
        groupService.getGroupByGroupId(groupId)

    @PostMapping(
        "/", consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun saveNewGroup(@Valid @RequestBody newGroup: GroupRequestDto): ResponseEntity<GroupResponseDto> {
        return ResponseEntity(groupService.saveNewGroup(newGroup), HttpHeaders(), HttpStatus.CREATED)
    }
}