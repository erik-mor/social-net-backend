package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.Admin
import com.vis.moravcik.socialnet.model.CreateUserDTO
import com.vis.moravcik.socialnet.model.PlayerPosition
import com.vis.moravcik.socialnet.model.User
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
@Repository
class AdminRepository(
        val template: JdbcOperations,
        val namedTemplate: NamedParameterJdbcOperations
) {
    fun findByUsername(username: String): Admin? {
        val result = template.query("select * from admins where username='$username'", ADMIN_MAPPER)
        return if (result.isEmpty()) null else result.first()
    }
}

private val ADMIN_MAPPER: RowMapper<Admin> = RowMapper { rs, _ ->
    Admin(
            id = rs.getInt("id"),
            username = rs.getString("username"),
            password = rs.getString("password")
    )
}
