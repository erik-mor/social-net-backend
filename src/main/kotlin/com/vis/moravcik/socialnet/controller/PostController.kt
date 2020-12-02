package com.vis.moravcik.socialnet.controller

import com.vis.moravcik.socialnet.model.CreatePostDTO
import com.vis.moravcik.socialnet.model.Post
import com.vis.moravcik.socialnet.repository.FollowerRepository
import com.vis.moravcik.socialnet.repository.PostRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp

@CrossOrigin(origins = ["http://localhost:3000"])
@Controller
@RequestMapping("/post")
class PostController(
        val postRepository: PostRepository,
        val followerRepository: FollowerRepository
) {

    @PostMapping("/addPost")
    fun addPost(@RequestBody post: CreatePostDTO): ResponseEntity<Int> {
        return ResponseEntity(postRepository.save(post), HttpStatus.CREATED)
    }

    @DeleteMapping("delete/{id}")
    fun deletePost(@PathVariable id: Int) {
        postRepository.delete(id)
    }

    // get all post by one user
    @GetMapping("/profile/{id}")
    fun getPostsByUser(@PathVariable id: Int): ResponseEntity<MutableList<Post>> {
        return ResponseEntity.ok(postRepository.findAllByUserId(id))
    }

    // get all posts from followed users for user
    @GetMapping("/home/{id}")
    fun getAllPosts(@PathVariable id: Int): ResponseEntity<MutableList<Post>> {
        val followedUsers = followerRepository.findFollowingByFollowerId(id)
        return ResponseEntity.ok(postRepository.findPostsByFollowedUsers(followedUsers))
    }
}
