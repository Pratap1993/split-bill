package com.chagu.splitbill.repository

import com.chagu.splitbill.model.BillEntity

interface BillRepositoryCustom {

    fun findListOfBillsForBalance(userId: Int): List<BillEntity>

    fun findListOfBillsForIndividualBalance(userId: Int, searchedUserId: Int): List<BillEntity>
}