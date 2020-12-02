package com.vis.moravcik.socialnet.controller

import com.vis.moravcik.socialnet.model.CreateUserDTO
import com.vis.moravcik.socialnet.model.User
import com.vis.moravcik.socialnet.repository.UserRepository
import com.vis.moravcik.socialnet.service.ArchivingService
import com.vis.moravcik.socialnet.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:3000"])
@Controller
@RequestMapping("/user")
class UserController(
        val userService: UserService,
        val userRepository: UserRepository,
        val archivingService: ArchivingService
) {
    @PostMapping("/register")
    fun register(@RequestBody user: CreateUserDTO): ResponseEntity<Response> {
        return userService.registerUser(user)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        return userService.loginUser(request)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Int): ResponseEntity<User> {
        val user = userRepository.findById(id)
        return if (user != null) {
            ResponseEntity(user, HttpStatus.FOUND)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/follow")
    fun follow(@RequestBody userRequest: FollowUserRequest): ResponseEntity<Response> {
        return ResponseEntity(userService.followUser(userRequest), HttpStatus.OK)
    }

    @PostMapping("/following")
    fun getUser(@RequestBody isFollowingRequest: IsFollowingRequest): ResponseEntity<Response> {
        return ResponseEntity(userService.isFollowing(isFollowingRequest), HttpStatus.OK)
    }

    @GetMapping("/discover/{id}")
    fun discover(@PathVariable id: Int): ResponseEntity<DiscoverResponse> {
        return ResponseEntity.ok(userService.discover(id))
    }

    @PostMapping("/archive")
    fun archive(@RequestBody archiveRequest: ArchiveDeleteRequest) {
        archivingService.archiveUsers(archiveRequest.ids)
    }

    @PostMapping("/batch-delete")
    fun batchDelete(@RequestBody deleteRequest: ArchiveDeleteRequest) {
        userService.batchDelete(deleteRequest.ids)
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<MutableList<User>> {
        return ResponseEntity.ok(userRepository.findAll())
    }

    @GetMapping("/not-archived")
    fun getAllNotArchived(): ResponseEntity<MutableList<User>> {
        return ResponseEntity.ok(userRepository.findAllNotArchived())
    }

}
// Request to follow user
data class FollowUserRequest(
        val follower_id: Int,
        val following_id: Int
)

// Check if user is following another user
data class IsFollowingRequest(
        val profile_id: Int,
        val user_id: Int
)

data class LoginRequest(
        val username: String,
        val password: String
)

// request from console application - delete or archive users
data class ArchiveDeleteRequest(
        val ids: List<Int>
)

// multi purpose response object
data class Response(
        val success: Boolean,
        val message: String
)

data class LoginResponse(
        var success: Boolean,
        var message: String,
        var id: Int?,
        var is_manager: Boolean?
)

// response with discovered users
data class DiscoverResponse(
        val success: Boolean,
        val users: List<User>?
)
