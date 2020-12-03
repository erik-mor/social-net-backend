package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.CreateUserDTO
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
class UserRepository(
        val template: JdbcOperations,
        val namedTemplate: NamedParameterJdbcOperations
) {
    fun save(user: CreateUserDTO): Int {
        val insertString = "insert into users(username, email, password, first_name, last_name, is_manager, longitude, latitude) values (?, ?, ?, ?, ?, ?, ?, ?);"
        return template.update(insertString, user.username, user.email, user.password, user.first_name, user.last_name, user.is_manager, user.long, user.lat)
    }

    fun delete(id: Int): Int {
        return template.update("delete from users where id=$id")
    }

    // delete list of users
    fun batchDelete(ids: List<Int>): Int {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        return namedTemplate.update("delete from users where id in (:ids)", parameters)
    }

    fun findByEmail(email: String): User? {
        val result = template.query("select * from users where email='$email'", USER_MAPPER)
        return if (result.isEmpty()) null else result.first()
    }

    fun findById(id: Int): User? {
        val result = template.query("select * from users where id=${id}", USER_MAPPER)
        return if (result.isEmpty()) null else result.first()
    }

    fun findByUsername(username: String): User? {
        val result = template.query("select * from users where username='$username'", USER_MAPPER)
        return if (result.isEmpty()) null else result.first()
    }

    // get list of users by ids
    fun getUsersByIds(ids: List<Int>): MutableList<User> {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        // if flag archive is set find only users that are not archived yet
        return namedTemplate.query("select * from users where id in (:ids)", parameters, USER_MAPPER)
     }

    fun discover(ids: List<Int>): List<User> {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        return namedTemplate.query(
                "select * from users where is_manager=false and longitude is not null and latitude is not null and id not in (:ids)",
                parameters,
                USER_MAPPER)
        }

    fun findAll(): MutableList<User> {
        return template.query(
                "select * from users",
                USER_MAPPER
        )
    }

    fun findAllNotArchived(): MutableList<User> {
        return template.query(
                "select * from users where is_archived=false",
                USER_MAPPER
        )
    }

    fun setArchived(is_archived: Boolean, ids: List<Int>) {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        namedTemplate.update("update users set is_archived=$is_archived where id in (:ids)", parameters)
    }

}

private val USER_MAPPER: RowMapper<User> = RowMapper { rs, _ ->
    User(
            id = rs.getInt("id"),
            email = rs.getString("email"),
            username = rs.getString("username"),
            password = rs.getString("password"),
            firstName = rs.getString("first_name"),
            lastName = rs.getString("last_name"),
            isManager = rs.getBoolean("is_manager"),
            longitude = rs.getDouble("longitude"),
            latitude = rs.getDouble("latitude"),
            isArchived = rs.getBoolean("is_archived")
    )
}
