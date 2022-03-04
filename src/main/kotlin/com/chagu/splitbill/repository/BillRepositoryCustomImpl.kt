package com.chagu.splitbill.repository

import com.chagu.splitbill.model.BillEntity
import com.chagu.splitbill.model.SplitDetailsEntity
import com.chagu.splitbill.model.UserEntity
import com.chagu.splitbill.utils.LoggerProvider
import org.slf4j.Logger
import javax.persistence.EntityGraph
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.*

class BillRepositoryCustomImpl(@PersistenceContext val entityManager: EntityManager) : BillRepositoryCustom {

    companion object {
        val LOG: Logger = LoggerProvider.getLogger(BillRepositoryCustomImpl::class.java)
    }

    override fun findListOfBillsForBalance(userId: Int): List<BillEntity> {
        LOG.info("Returning list of BillEntity for userId : {}", userId)
        var builder: CriteriaBuilder = entityManager.criteriaBuilder
        var query: CriteriaQuery<BillEntity> = builder.createQuery(BillEntity::class.java)
        var root = query.from(BillEntity::class.java)
        var splitDetails: Join<BillEntity, SplitDetailsEntity> = root.join("splitDetails", JoinType.INNER)
        var paidByUser: Join<SplitDetailsEntity, UserEntity> = splitDetails.join("paidBy", JoinType.INNER)
        var paidToUser: Join<SplitDetailsEntity, UserEntity> = splitDetails.join("paidTo", JoinType.INNER)

        val isSettled: Path<Set<Boolean>> = root.get("isSettled")
        val isActive: Path<Set<Boolean>> = root.get("isActive")
        val paidByUserId: Path<Set<Int>> = paidByUser.get("id")
        val paidToUserId: Path<Set<Int>> = paidToUser.get("id")

        var isSettlePredicate: Predicate = builder.equal(isSettled, false)
        var isActivePredicate: Predicate = builder.equal(isActive, true)
        var paidByUserPred: Predicate = builder.equal(paidByUserId, userId)
        var paidToUserPred: Predicate = builder.equal(paidToUserId, userId)
        var userInEither: Predicate = builder.or(paidByUserPred, paidToUserPred)
        var finalPredicate: Predicate = builder.and(isSettlePredicate, isActivePredicate, userInEither)

        val entityGraph: EntityGraph<BillEntity> = entityManager.createEntityGraph(BillEntity::class.java)
        entityGraph.addSubgraph("splitDetails", SplitDetailsEntity::class.java)
            .addAttributeNodes("paidBy", "paidTo")

        query.select(root).where(builder.and(finalPredicate))
        return entityManager.createQuery(query).setHint("javax.persistence.fetchgraph", entityGraph).resultList
    }

    override fun findListOfBillsForIndividualBalance(userId: Int, searchedUserId: Int): List<BillEntity> {
        LOG.info("Returning list of BillEntity for userId : $userId and searchedUSerId: $searchedUserId")
        var builder: CriteriaBuilder = entityManager.criteriaBuilder
        var query: CriteriaQuery<BillEntity> = builder.createQuery(BillEntity::class.java)
        var root = query.from(BillEntity::class.java)
        var splitDetails: Join<BillEntity, SplitDetailsEntity> = root.join("splitDetails", JoinType.INNER)
        var paidByUser: Join<SplitDetailsEntity, UserEntity> = splitDetails.join("paidBy", JoinType.INNER)
        var paidToUser: Join<SplitDetailsEntity, UserEntity> = splitDetails.join("paidTo", JoinType.INNER)

        val isSettled: Path<Set<Boolean>> = root.get("isSettled")
        val isActive: Path<Set<Boolean>> = root.get("isActive")
        var isActivePredicate: Predicate = builder.equal(isActive, true)
        val paidByUserId: Path<Set<Int>> = paidByUser.get("id")
        val paidToUserId: Path<Set<Int>> = paidToUser.get("id")

        val setIn: Set<Int> = setOf(userId, searchedUserId)
        var inPaidBy = builder.`in`(paidByUserId)
        var inPaidTo = builder.`in`(paidToUserId)
        inPaidBy.value(setIn)
        inPaidTo.value(setIn)

        var isSettlePredicate: Predicate = builder.equal(isSettled, false)
        var finalPredicate: Predicate = builder.and(isSettlePredicate, isActivePredicate, inPaidBy, inPaidTo)

        val entityGraph: EntityGraph<BillEntity> = entityManager.createEntityGraph(BillEntity::class.java)
        entityGraph.addSubgraph("splitDetails", SplitDetailsEntity::class.java)
            .addAttributeNodes("paidBy", "paidTo")

        query.select(root).where(builder.and(finalPredicate))
        return entityManager.createQuery(query).setHint("javax.persistence.fetchgraph", entityGraph).resultList
    }

}