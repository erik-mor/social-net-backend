package com.vis.moravcik.socialnet.service

import com.vis.moravcik.socialnet.controller.ContactUserRequest
import com.vis.moravcik.socialnet.controller.OpenChannelsResponse
import com.vis.moravcik.socialnet.controller.SendMessageRequest
import com.vis.moravcik.socialnet.model.ChannelUser
import com.vis.moravcik.socialnet.model.Message
import com.vis.moravcik.socialnet.repository.ChannelRepository
import com.vis.moravcik.socialnet.repository.ChannelUserRepository
import com.vis.moravcik.socialnet.repository.MessageRepository
import com.vis.moravcik.socialnet.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class MessagingService(
        val channelUserRepository: ChannelUserRepository,
        val messageRepository: MessageRepository,
        val userRepository: UserRepository
) {

    fun getChannelsForUser(userId: Int): List<OpenChannelsResponse> {
        val channels = channelUserRepository.getChannelIdByUser(userId)
        if (channels.isEmpty()) {
            return listOf()
        }
        val channelUsers = channelUserRepository.getChannelUsersByChannelsAndUserId(userId, channels)

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