package com.vis.moravcik.socialnet.model

import javax.persistence.*

class FollowerFollowing(
        val id: Int,
        var followerId: Int,
        var followingId: Int
)

data class CreateFollowerFollowing (
        var followerId: Int,
        var followingId: Int
)