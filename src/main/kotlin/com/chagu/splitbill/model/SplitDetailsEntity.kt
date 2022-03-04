package com.chagu.splitbill.model

import javax.persistence.*

@Entity
@Table(name = "split_bill_details")
class SplitDetailsEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    var billEntity: BillEntity? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_by", referencedColumnName = "id")
    var paidBy: UserEntity? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_to", referencedColumnName = "id")
    var paidTo: UserEntity? = null

    @Column(name = "amount_paid")
    var amountPaid: Long? = null

    @Column(name = "is_settled")
    var isSettled: Boolean? = false

}