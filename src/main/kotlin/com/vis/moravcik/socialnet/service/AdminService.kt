package com.vis.moravcik.socialnet.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.vis.moravcik.socialnet.controller.ChannelsResponse
import com.vis.moravcik.socialnet.controller.LoginRequest
import com.vis.moravcik.socialnet.controller.Response
import com.vis.moravcik.socialnet.repository.*
import com.vis.moravcik.socialnet.xml.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AdminService(
        val userRepository: UserRepository,
        val adminRepository: AdminRepository,
        val postRepository: PostRepository,
        val xmlWriter: XmlWriter,
        val channelRepository: ChannelRepository,
        val channelUserRepository: ChannelUserRepository,
        val messageRepository: MessageRepository
) {
    fun loginAdmin(request: LoginRequest): ResponseEntity<Response> {
        // check if username is present
        val admin = adminRepository.findByUsername(username = request.username)
                ?: return ResponseEntity.ok(Response(false, "User does not exist"))

        // check if password is correct
        val result: BCrypt.Result = BCrypt.verifyer().verify(request.password.toCharArray(), admin.password)

        return if (result.verified) {
            ResponseEntity.ok(Response(true, "Login successful"))
        } else {
            ResponseEntity.ok(Response(false, "Wrong password"))
        }
    }

    fun archiveUsers(ids: List<Int>){
        // get users that are requested to archiving and filter already archived
        val users = userRepository.getUsersByIds(ids).filter {
            !it.isArchived
        }
        if (users.isEmpty()) return

        // set flag `is_archived` to true in database so users will not be more queried for archiving
        userRepository.setArchived(true, ids)

        // getting posts for each user and mapping user to object that will be saved in xml
        val xmlUsers = users.map {
            XMLUser(
                    id = it.id,
                    username = it.username,
                    email = it.email,
                    password = it.password,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    is_manager = it.isManager,
                    longitude = it.longitude,
                    latitude = it.latitude,
                    posts = postRepository.findAllByUserId(it.id).map {
                        post -> XMLPost(id = post.id, user_id = post.user_id, content = post.content)
                    }
            )
        }.toMutableList()

        xmlWriter.saveUsers(xmlUsers)
    }

    fun archiveChannels(ids: List<Int>) {
        // filter already archived channels
        val filteredChannels = ids.filter {
            channelRepository.getChannelById(it)?.isArchived == false
        }
        if (filteredChannels.isEmpty()) return

        // set flag `is_archived` to true in database so channels will not be more queried for archiving
        channelRepository.setArchived(true, filteredChannels)

        // get users and messages for every channel and mapping to xml objects
        val channels = filteredChannels.map {
            val channelsUsers = channelUserRepository.getAllByChannelId(it)
            val messages = messageRepository.getMessagesByChannel(it).map {
                message ->  XMLMessage(message.id, message.senderId, message.message)
            }
            XMLChannel(it, channelsUsers[0].userId, channelsUsers[1].userId, messages)
        }.toMutableList()

        xmlWriter.saveChannels(channels)
    }

    fun findAllNotArchived(): List<ChannelsResponse> {
        val channels = channelRepository.findAllNotArchived()
        return channels.map {
            val channelUsers = channelUserRepository.getAllByChannelId(it)
            val user1 = userRepository.findById(channelUsers[0].userId)
            val user2 = userRepository.findById(channelUsers[1].userId)

            ChannelsResponse(it, user1, user2)
        }
    }
}