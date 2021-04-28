package com.vis.moravcik.socialnet.controller

import com.vis.moravcik.socialnet.model.Channel
import com.vis.moravcik.socialnet.repository.ChannelRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/test")
class TestController(
    val channelRepository: ChannelRepository
) {
    @GetMapping("/getChannels")
    fun getChannels(): ResponseEntity<MutableList<Channel>> {
        return ResponseEntity.ok(channelRepository.selectAll())
    }

}