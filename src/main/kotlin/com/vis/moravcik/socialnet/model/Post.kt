package com.vis.moravcik.socialnet.model

class Post (
        val id: Int,
        var user_id: Int,
        var content: String
)

data class CreatePostDTO(
        val user_id: Int,
        val content: String
)
