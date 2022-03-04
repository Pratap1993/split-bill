package com.chagu.splitbill.repository

import com.chagu.splitbill.model.SplitDetailsEntity

interface SplitDetailsRepositoryCustom {

    fun findSplitDetailsForSettlement(billId: Int, paidByUserId: Int, paidToUserId: Int): SplitDetailsEntity

}