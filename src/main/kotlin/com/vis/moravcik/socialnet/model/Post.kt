package com.vis.moravcik.socialnet.model

class Post (
        val id: Int,
        var content: String,
        var likes: Int,
        var comments: List<Comment>
)
data class GetPost(
        val userId: Int,
        val firstName: String,
        val lastName: String,
        val postId: Int,
        val content: String,
        val comments: Int,
        val likes: Int
)

data class UserPost(
        val content: String,
        val comments: Int,
        val likes: Int
)



