package com.chagu.splitbill.repository

import com.chagu.splitbill.model.BillEntity
import com.chagu.splitbill.model.SplitDetailsEntity
import com.chagu.splitbill.model.UserEntity
import com.chagu.splitbill.utils.LoggerProvider
import org.slf4j.Logger
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.*

class SplitDetailsRepositoryCustomImpl(
    @PersistenceContext
    val entityManager: EntityManager
) : SplitDetailsRepositoryCustom {

    companion object {
        val LOG: Logger = LoggerProvider.getLogger(SplitDetailsRepositoryCustomImpl::class.java)
    }

    override fun findSplitDetailsForSettlement(billId: Int, paidById: Int, paidToId: Int): SplitDetailsEntity {
        LOG.info("Returning SplitDetails For Settlement")
        var builder: CriteriaBuilder = entityManager.criteriaBuilder
        var query: CriteriaQuery<SplitDetailsEntity> = builder.createQuery(SplitDetailsEntity::class.java)
        var root = query.from(BillEntity::class.java)
        var splitDetails: Join<BillEntity, SplitDetailsEntity> = root.join("splitDetails", JoinType.INNER)
        var paidByUser: Join<SplitDetailsEntity, UserEntity> = splitDetails.join("paidBy", JoinType.INNER)
        var paidToUser: Join<SplitDetailsEntity, UserEntity> = splitDetails.join("paidTo", JoinType.INNER)

        val isSettled: Path<Set<Boolean>> = root.get("isSettled")
        val isActive: Path<Set<Boolean>> = root.get("isActive")
        val billIdPath: Path<Set<Int>> = root.get("id")
        val paidByUserId: Path<Set<Int>> = paidByUser.get("id")
        val paidToUserId: Path<Set<Int>> = paidToUser.get("id")

        var billIdPredicate: Predicate = builder.equal(billIdPath, billId)
        var isSettlePredicate: Predicate = builder.equal(isSettled, false)
        var isActivePredicate: Predicate = builder.equal(isActive, true)

        var paidByUserPred: Predicate = builder.equal(paidByUserId, paidById)
        var paidToUserPred: Predicate = builder.equal(paidToUserId, paidToId)
        var finalPredicate: Predicate =
            builder.and(billIdPredicate, isSettlePredicate, isActivePredicate, paidByUserPred, paidToUserPred)

        query.select(splitDetails).where(builder.and(finalPredicate))
        return entityManager.createQuery(query).singleResult
    }
}