package com.vis.moravcik.socialnet.controller

import com.vis.moravcik.socialnet.model.Message
import com.vis.moravcik.socialnet.repository.ChannelRepository
import com.vis.moravcik.socialnet.service.MessagingService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:3000"])
@Controller
@RequestMapping("/messaging")
class MessagingController(
        val messagingService: MessagingService,
        val channelRepository: ChannelRepository
) {
    // contact user
    @PostMapping("/contact")
    fun contact(@RequestBody contactUserRequest: ContactUserRequest): ResponseEntity<Int> {
        return ResponseEntity.ok(channelRepository.saveChannel(contactUserRequest.user_id, contactUserRequest.contacted_user_id)!!)
    }

    // get all open channels for user
    @GetMapping("channels/{id}")
    fun getChannels(@PathVariable id: Int): ResponseEntity<List<OpenChannelsResponse>> {
        return ResponseEntity.ok(messagingService.getChannelsForUser(id))
    }

    // get all messages by one channel
    @GetMapping("/messages/{id}")
    fun getMessages(@PathVariable id: Int): ResponseEntity<MutableList<Message>> {
        return ResponseEntity.ok(messagingService.getMessagesForChannel(id))
    }

    // send message to channel
    @PostMapping("/send")
    fun sendMessage(@RequestBody messageRequest: SendMessageRequest): ResponseEntity<String> {
        messagingService.saveMessage(messageRequest)
        return ResponseEntity.ok("Ok")
    }
}

data class ContactUserRequest(
        val user_id: Int,
        val contacted_user_id: Int
)

data class SendMessageRequest(
        val sender_id: Int,
        val channel_id: Int,
        val message: String
)

data class OpenChannelsResponse(
        val channel_id: Int,
        val user_id: Int,
        val firstName: String,
        val lastName: String
)
