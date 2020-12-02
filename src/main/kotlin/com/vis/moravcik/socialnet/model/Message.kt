package com.vis.moravcik.socialnet.model

import java.sql.Timestamp

class Message (
        val id: Int,
        var message: String,
        var channelId: Int,
        var senderId: Int
)
