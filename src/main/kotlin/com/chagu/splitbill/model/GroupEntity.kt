package com.chagu.splitbill.model

import javax.persistence.*

@Entity
@Table(name = "groups")
class GroupEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "group_name")
    var groupName: String? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "group_users",
        joinColumns = [JoinColumn(name = "group_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")]
    )
    var users: List<UserEntity> = mutableListOf<UserEntity>()

}