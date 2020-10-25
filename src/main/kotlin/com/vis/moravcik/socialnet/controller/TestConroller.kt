package com.vis.moravcik.socialnet.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TestConroller {

    @GetMapping("/test")
    fun addUser(): ResponseEntity<String> {
        return ResponseEntity.ok("OK")
    }
}