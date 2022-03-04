package com.chagu.splitbill.repository

import com.chagu.splitbill.model.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<GroupEntity, Int> {

    fun findByGroupName(groupName: String)
}