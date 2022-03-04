package com.chagu.splitbill.repository

import com.chagu.splitbill.model.SplitDetailsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SplitDetailsRepository : JpaRepository<SplitDetailsEntity, Int>, SplitDetailsRepositoryCustom