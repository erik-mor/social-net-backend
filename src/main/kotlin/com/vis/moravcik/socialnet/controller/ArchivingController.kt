package com.vis.moravcik.socialnet.controller

import com.vis.moravcik.socialnet.model.User
import com.vis.moravcik.socialnet.service.ArchivingService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/arch")
@Controller
class ArchivingController(
        val archivingService: ArchivingService
) {
    @PostMapping("/users")
    fun archiveUsers(@RequestBody archiveRequest: ArchiveDeleteRequest) {
        archivingService.archiveUsers(archiveRequest.ids)
    }

    @PostMapping("/channels")
    fun archiveChannels(@RequestBody archiveRequest: ArchiveDeleteRequest) {
        archivingService.archiveChannels(archiveRequest.ids)
    }

    @GetMapping("/channels/not-archived")
    fun getAllNotArchived(): ResponseEntity<List<ChannelsResponse>> {
        return ResponseEntity.ok(archivingService.findAllNotArchived())
    }
}

data class ChannelsResponse(
        val id: Int,
        val user1: User?,
        val user2: User?
)