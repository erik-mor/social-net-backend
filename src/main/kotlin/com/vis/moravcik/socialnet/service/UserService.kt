package com.vis.moravcik.socialnet.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.vis.moravcik.socialnet.controller.*
import com.vis.moravcik.socialnet.model.CreateFollowerFollowing
import com.vis.moravcik.socialnet.model.CreateUserDTO
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
        val postRepository: PostRepository,
        val messageRepository: MessageRepository,
        val channelRepository: ChannelRepository,
        val channelUserRepository: ChannelUserRepository
) {

    fun registerUser(createUserDTO: CreateUserDTO): ResponseEntity<Response> {
        // check if email is present in db
        var user = userRepository.findByEmail(createUserDTO.email)
        if (user != null) {
            return ResponseEntity.ok(Response(false, "email already in use"))
        }

        // check if username is present in db
        user = userRepository.findByUsername(createUserDTO.username)
        if (user != null) {
            return ResponseEntity.ok(Response(false, "username already in use"))
        }
        val hashedPass = BCrypt.withDefaults().hashToString(12, createUserDTO.password.toCharArray())

        createUserDTO.password = hashedPass
        userRepository.save(createUserDTO)
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

    fun followUser(userRequest: FollowUserRequest): Response {
        //check if exists entry in followers_following - thus user is following another user
        val item = followerRepository.findByFollowerIdAndFollowingId(followingId = userRequest.following_id, followerId = userRequest.follower_id)

        // if not add entry in followers_following
        return if (item == null) {
            followerRepository.save(CreateFollowerFollowing(followerId = userRequest.follower_id, followingId = userRequest.following_id))
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

    // discover feature for managers - find players that have their location stored and are not yet followed by manager
    fun discover(id: Int): DiscoverResponse {
        // get followed users to exclude them in the search
        val ids = followerRepository.findFollowingByFollowerId(id)
        // add managers id
        ids.add(id)

        // get manager that is requesting discover
        val manager = userRepository.findById(id)

        // get users excluding followed ids
        val discoveredUsers = userRepository.discover(ids)

        return if (discoveredUsers.isNotEmpty()) {
            // sort by their location
            val sortedUsers = discoveredUsers.sortedWith(CustomComparator(manager?.longitude!!, manager.latitude!!))
            DiscoverResponse(success = true, users = sortedUsers)
        } else {
            DiscoverResponse(success = false, users = null)
        }
    }

    // complete delete of user
    fun batchDelete(ids: List<Int>) {
        val channels = channelUserRepository.getChannelsByUsers(ids)
        if (channels.isNotEmpty()) {
            messageRepository.deleteByChannels(channels)
            channelUserRepository.deleteByChannelId(channels)
            channelRepository.deleteByChannelId(channels)
        }
        followerRepository.deleteByUserIds(ids)
        postRepository.deleteByUsers(ids)
        userRepository.batchDelete(ids)
    }
}

// comparator to sort users by location - longitude and latitude
class CustomComparator(
        private val long: Double,
        private val lat: Double
) : Comparator<User?> {
    override fun compare(o1: User?, o2: User?): Int {
        return distance(o1?.latitude!!, o1.longitude!!) - distance(o2?.latitude!!, o2.longitude!!)
    }

    private fun distance(lat2: Double, lon2: Double): Int {
        return if ((lat == lat2) && (long == lon2)) {
            0
        } else {
            val theta: Double = long - lon2
            var dist = sin(Math.toRadians(lat)) * sin(Math.toRadians(lat2)) + cos(Math.toRadians(lat)) * cos(Math.toRadians(lat2)) * cos(Math.toRadians(theta))
            dist = acos(dist)
            dist = Math.toDegrees(dist)
            dist *= 60 * 1.1515
            dist *= 1.609344
            dist.toInt()
        }
    }
}


