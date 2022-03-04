package com.chagu.splitbill.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "bill")
class BillEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "bill_against")
    var billAgainst: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    var group: GroupEntity? = null

    @Column(name = "total_amount")
    var totalAmount: Long? = null

    @Column(name = "amount_per_head")
    var amountPerHead: Long? = null

    @Column(name = "bill_date")
    var billDateTime: LocalDateTime? = null

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "billEntity", orphanRemoval = true, cascade = [CascadeType.ALL])
    var splitDetails = mutableListOf<SplitDetailsEntity>()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", referencedColumnName = "id")
    var lastModifiedBy: UserEntity? = null

    @Column(name = "last_modified_date")
    var lastModifiedDate: LocalDateTime? = null

    @Column(name = "is_settled")
    var isSettled: Boolean? = false

    @Column(name = "is_active")
    var isActive: Boolean = true
}