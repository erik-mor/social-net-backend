package com.vis.moravcik.socialnet.model

import com.fasterxml.jackson.annotation.JsonProperty

class User (
        val id: Int,
        var username: String,
        var email: String,
        var password: String,
        var firstName: String,
        var lastName: String,
        var club: String,
        var position: PlayerPosition?,
        var isManager: Boolean,
        var longitude: Double?,
        var latitude: Double?,
        var isArchived: Boolean
)

enum class PlayerPosition {
    ST,
    GK,
    CM,
    CB,
    LM,
    RW,
    LB,
    RB
}

data class CreateUserDTO(
        val username: String,
        val email: String,
        var password: String,
        val first_name: String,
        val last_name: String,
        val is_manager: Boolean,
        val club: String,
        val position: PlayerPosition?,
        val long: Double?,
        val lat: Double?
)