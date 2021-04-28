package com.vis.moravcik.socialnet.model

import javax.persistence.*

class FollowerFollowing(
        var followerId: Int,
        var followingId: Int
)
