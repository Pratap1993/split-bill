package com.chagu.splitbill.repository

import com.chagu.splitbill.model.BillEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BillRepository : JpaRepository<BillEntity, Int>, BillRepositoryCustom