package com.vis.moravcik.socialnet.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.vis.moravcik.socialnet.EmailServiceImpl
import com.vis.moravcik.socialnet.controller.*
import com.vis.moravcik.socialnet.model.FollowerFollowing
import com.vis.moravcik.socialnet.model.User
import com.vis.moravcik.socialnet.repository.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


@Service
class UserService(
        val userRepository: UserRepository,
        val followerRepository: FollowerRepository,
        val emailServiceImpl: EmailServiceImpl
) {

    fun registerUser(userToCreate: User): ResponseEntity<Response> {
//         check if email is present in db
        var user = userRepository.findByEmail(userToCreate.email)
        if (user != null) {
            return ResponseEntity.ok(Response(false, "email already in use"))
        }

        // check if username is present in db
        user = userRepository.findByUsername(userToCreate.username)
        if (user != null) {
            return ResponseEntity.ok(Response(false, "username already in use"))
        }
        val hashedPass = BCrypt.withDefaults().hashToString(12, userToCreate.password.toCharArray())

        userToCreate.password = hashedPass
        userRepository.save(userToCreate)
        return ResponseEntity.ok(Response(true, "Registration successful. You can now login."))
    }

    fun loginUser(loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        // check if username is present
        val user = userRepository.findByUsername(username = loginRequest.username)
                ?: return ResponseEntity.ok(LoginResponse(false, "User does not exist", null, null))

        // check if password is correct
        val result: BCrypt.Result = BCrypt.verifyer().verify(loginRequest.password.toCharArray(), user.password)

        return if (result.verified) {
            ResponseEntity.ok(LoginResponse(true, "Login successful", user.id, user.isManager))
        } else {
            ResponseEntity.ok(LoginResponse(false, "Wrong password", user.id, user.isManager))
        }
    }

    fun sendMail(userId: Int) {
        val followers = userRepository.getFollowers(userId)
        val username = userRepository.findById(userId)?.username
        for (follower in followers) {
            val mail = userRepository.getMail(follower.id, username)
                ?: "Text"
            print(mail)
            emailServiceImpl.sendSimpleMessage("erik.moravcik.st@vsb.cz", "Notification", mail)
        }
    }

    fun followUser(userRequest: FollowUserRequest): Response {
        //check if exists entry in followers_following - thus user is following another user
        val item = followerRepository.findByFollowerIdAndFollowingId(followingId = userRequest.following_id, followerId = userRequest.follower_id)

        // if not add entry in followers_following
        return if (item == null) {
            followerRepository.save(FollowerFollowing(followerId = userRequest.follower_id, followingId = userRequest.following_id))
            Response(success = true, message = "User id: ${userRequest.follower_id} is now following user id: ${userRequest.following_id}")
        } else {
            Response(success = false, message = "User id: ${userRequest.follower_id} is already following user id: ${userRequest.following_id}")
        }
    }

    // check if user is following another user
    fun isFollowing(isFollowingRequest: IsFollowingRequest): Response {
        val item = followerRepository.findByFollowerIdAndFollowingId(followerId = isFollowingRequest.user_id, followingId = isFollowingRequest.profile_id)
        return if (item == null) {
            Response(success = false, message = "User id: ${isFollowingRequest.user_id} is not following user id: ${isFollowingRequest.profile_id}")
        } else {
            Response(success = true, message = "User id: ${isFollowingRequest.user_id} is following user id: ${isFollowingRequest.profile_id}")
        }
    }

}

