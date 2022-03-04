package com.chagu.splitbill.model

import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    @Column(name = "email", unique = true)
    var email: String? = null

    @Column(name = "ph_no", unique = true)
    var mobile: String? = null

    @Column(name = "is_active")
    var isActive: Boolean? = false
}