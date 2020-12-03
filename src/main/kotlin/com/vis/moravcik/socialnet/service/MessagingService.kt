package com.vis.moravcik.socialnet.service

import com.vis.moravcik.socialnet.controller.ContactUserRequest
import com.vis.moravcik.socialnet.controller.OpenChannelsResponse
import com.vis.moravcik.socialnet.controller.SendMessageRequest
import com.vis.moravcik.socialnet.model.ChannelUser
import com.vis.moravcik.socialnet.model.Message
import com.vis.moravcik.socialnet.repository.ChannelRepository
import com.vis.moravcik.socialnet.repository.MessageRepository
import com.vis.moravcik.socialnet.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class MessagingService(
        val channelRepository: ChannelRepository,
        val messageRepository: MessageRepository,
        val userRepository: UserRepository
) {
    fun contact(contactUserRequest: ContactUserRequest): Int {
        // condition so user cannot contact himself
        if (contactUserRequest.user_id == contactUserRequest.contacted_user_id)
            return 0

        // if channel already exists return it or create new for requesting users
        return channelRepository.getChannelByUsers(contactUserRequest.user_id, contactUserRequest.contacted_user_id)
            ?: createChannel(contactUserRequest)
    }

    fun createChannel(contactUserRequest: ContactUserRequest): Int {
        // create new channel
        val channelId = channelRepository.saveChannel()

        // create entries in channel_users for requesting users
        channelRepository.saveUsers(channelId, contactUserRequest.user_id, contactUserRequest.contacted_user_id)
        return channelId
    }

    fun getChannelsForUser(userId: Int): List<OpenChannelsResponse> {
        val channels = channelRepository.getChannelsByUserId(userId)
        val channelUsers = channelRepository.getChannelUsersByChannelIdAndUserId(userId, channels)

        return channelUsers.map {
            val user = userRepository.findById(it.userId)
            OpenChannelsResponse(it.channelId, it.userId, user!!.firstName, user.lastName)
        }

    }

    fun getMessagesForChannel(channelId: Int): MutableList<Message> {
        return messageRepository.getMessagesByChannel(channelId)
    }

    fun saveMessage(messageRequest: SendMessageRequest) {
        messageRepository.saveMessage(message = messageRequest.message, channelId = messageRequest.channel_id, senderId = messageRequest.sender_id)
    }
}