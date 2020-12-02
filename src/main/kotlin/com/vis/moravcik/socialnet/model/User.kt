package com.vis.moravcik.socialnet.model

import com.fasterxml.jackson.annotation.JsonProperty

class User (
        val id: Int,
        var username: String,
        var email: String,
        var password: String,
        var firstName: String,
        var lastName: String,
        var isManager: Boolean,
        var longitude: Double?,
        var latitude: Double?,
        var isArchived: Boolean
)

data class CreateUserDTO(
        val username: String,
        val email: String,
        val password: String,
        val first_name: String,
        val last_name: String,
        val is_manager: Boolean,
        val long: Double?,
        val lat: Double?
)