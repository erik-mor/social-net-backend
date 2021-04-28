package com.vis.moravcik.socialnet.controller

import com.vis.moravcik.socialnet.model.GetPost
import com.vis.moravcik.socialnet.model.Like
import com.vis.moravcik.socialnet.repository.LikeRepository
import com.vis.moravcik.socialnet.repository.PostRepository
import com.vis.moravcik.socialnet.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:3000"])
@Controller
@RequestMapping("/post")
class PostController(
        val postRepository: PostRepository,
        val likeRepository: LikeRepository,
        val userService: UserService
) {

    @PostMapping("/addPost")
    fun addPost(@RequestBody post: CreatePost): ResponseEntity<Int> {
        userService.sendMail(post.user_id)
        return ResponseEntity(postRepository.save(post), HttpStatus.CREATED)
    }

    @DeleteMapping("delete/{id}")
    fun deletePost(@PathVariable id: Int) {
        postRepository.delete(id)
    }

    // get all post by one user
    @GetMapping("/profile/{id}")
    fun getPostsByUser(@PathVariable id: Int): ResponseEntity<MutableList<GetPost>> {
        return ResponseEntity.ok(postRepository.getPostByFollowedUsers(id))
    }


    @PostMapping("/adLike")
    fun addLike(@RequestBody like: Like): ResponseEntity<Int> {
        return ResponseEntity(likeRepository.save(like), HttpStatus.CREATED)
    }
}

data class CreatePost(
    val content: String,
    val user_id: Int
)

data class CreateComment(
    val user_id: Int,
    val post_id: Int,
    val content: String
)