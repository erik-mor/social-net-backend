package com.vis.moravcik.socialnet.controller

import com.vis.moravcik.socialnet.repository.UserRepository
import com.vis.moravcik.socialnet.model.User
import com.vis.moravcik.socialnet.model.UserType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/user")
class TestConroller(
        val userRepository: UserRepository
) {

    @PostMapping("/addUser")
    fun addUser(@RequestBody user: CreateUserDTO): ResponseEntity<User> {
        val userToCreate = User(
                firstName = user.firstName,
                lastName = user.lastName,
                type = user.type
        )
        return ResponseEntity(userRepository.save(userToCreate), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<User> {
        val user = userRepository.findById(id)
        if (user.isPresent) {
            return ResponseEntity(user.get(), HttpStatus.FOUND)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

    }
}

data class CreateUserDTO(
        val firstName: String,
        val lastName: String,
        val type: UserType
)
