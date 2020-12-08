package com.vis.moravcik.socialnet.controller

import com.vis.moravcik.socialnet.model.User
import com.vis.moravcik.socialnet.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/admin")
@Controller
class AdminController(
        val adminService: AdminService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Response> {
        return adminService.loginAdmin(request)
    }

    @PostMapping("/archive/users")
    fun archiveUsers(@RequestBody archiveRequest: ArchiveDeleteRequest) {
        adminService.archiveUsers(archiveRequest.ids)
    }

    @PostMapping("/archive/channels")
    fun archiveChannels(@RequestBody archiveRequest: ArchiveDeleteRequest) {
        adminService.archiveChannels(archiveRequest.ids)
    }

    @GetMapping("/channels/not-archived")
    fun getAllNotArchived(): ResponseEntity<List<ChannelsResponse>> {
        return ResponseEntity.ok(adminService.findAllNotArchived())
    }
}

data class ChannelsResponse(
        val id: Int,
        val user1: User?,
        val user2: User?
)