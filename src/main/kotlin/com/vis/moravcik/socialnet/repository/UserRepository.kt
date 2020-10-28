package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>
