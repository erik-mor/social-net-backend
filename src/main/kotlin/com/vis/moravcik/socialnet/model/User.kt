package com.vis.moravcik.socialnet.model

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javax.persistence.*

@Entity
@Table(name = "users")
class User (
        @Column(name = "first_name")
        var firstName: String,

        @Column(name = "last_name")
        var lastName: String,

        @Column(name = "type")
        @Enumerated(EnumType.STRING)
        var type: UserType

) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Long = 0

}

enum class UserType {
    @JsonAlias("player")
    PLAYER,
    @JsonAlias("manager")
    MANAGER;
}
