package com.vis.moravcik.socialnet.model

import java.sql.Date

class User (
        val id: Int,
        var username: String,
        var email: String,
        var password: String,
        var firstName: String,
        var lastName: String,
        var birth_date: Date,
        var club: String,
        var isManager: Boolean,
        var longitude: Double,
        var latitude: Double,
        var birthday_notification: Boolean,
        var posts: List<UserPost>?
)
