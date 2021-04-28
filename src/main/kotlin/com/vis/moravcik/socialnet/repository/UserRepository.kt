package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.User
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.sql.*
import java.sql.Array

@Component
@Repository
class UserRepository(
        val template: JdbcOperations,
        val postRepository: PostRepository
) {
    fun save(user: User): Int? {
        return template.execute { connection: Connection ->
            val statement = connection.prepareStatement(
    """insert into
                users(username, email, password, first_name, last_name, club, is_manager, longitude, latitude, birth_date, birthday_notification)
                    values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
            )
            statement.setString(1, user.username)
            statement.setString(2, user.email)
            statement.setString(3, user.password)
            statement.setString(4, user.firstName)
            statement.setString(5, user.lastName)
            statement.setString(6, user.club)
            statement.setBoolean(7, user.isManager)
            statement.setDouble(8, user.longitude)
            statement.setDouble(9, user.latitude)
            statement.setDate(10, user.birth_date)
            statement.setBoolean(11, user.birthday_notification)
            statement.executeUpdate()
        }
    }

    fun delete(id: Int) {
        val result = template.execute { connection: Connection ->
            val statement = connection.prepareStatement(
                "select * from users where id=?"
            )
            statement.setInt(1, id)
            statement.execute()
        }
    }

    fun discover(id: Int): MutableList<Discover> {
        return template.query({ conn: Connection ->
            val sql =
                """
        select following.id, following.username, following.first_name, following.last_name, following.latitude, following.longitude, [dbo].distance(manager.latitude, manager.longitude, following.latitude, following.longitude) as distance
        from (
              select *
              from users
              where id not in (
                  select following_id
                  from follower_following
                  where follower_id = 1 and following_id != 1
              ) and id != 1 and is_manager = 0
        ) as following,
        (
        select TOP 1 *
        from users
        where id = ?
        ) as manager
        order by distance
                """
            val statement = conn.prepareStatement(sql)
            statement.setInt(1, id)
            statement
        }, discover)
    }

    fun findByEmail(email: String): User? {
        val result = template.query({ connection: Connection ->
            val statement = connection.prepareStatement(
                "select * from users where email=?"
            )
            statement.setString(1, email)
            statement
        }, x)
        return if (result.isEmpty()) null else result.first()
    }

    fun findById(id: Int): User? {
        val result = template.query({ connection: Connection ->
            val statement = connection.prepareStatement(
                "select * from users where id=?"
            )
            statement.setInt(1, id)
            statement
        }, x)
        return if (result.isEmpty()) null else {
            val user = result.first()
            val posts = postRepository.findAllByUserId(user.id)
            user.posts = posts
            user
        }
    }

    fun findByUsername(username: String): User? {
        val result = template.query({ connection: Connection ->
            val statement = connection.prepareStatement(
                "select * from users where username=?"
            )
            statement.setString(1, username)
            statement
        }, x)
        return if (result.isEmpty()) null else result.first()
    }

    fun getFollowers(userId: Int): MutableList<User> {
        val sql = "select * from users where id in (select follower_id  from follower_following where following_id = ?)"
        return template.query({ con: Connection ->
            val statement = con.prepareStatement(sql)
            statement.setInt(1, userId)
            statement
        }, x)
    }

    fun update(user: User) {
        val sql = """
           update users
            set first_name=?,
                last_name=?,
                username=?,
                email=?,
                password=?,
                birth_date=?,
                club=?,
                is_manager=?,
                birthday_notification=?,
                longitude=?,
                latitude=?
            where id = ?
        """
        template.update { con: Connection ->
            val statement = con.prepareStatement(sql)
            statement.setString(1, user.firstName)
            statement.setString(2, user.lastName)
            statement.setString(3, user.username)
            statement.setString(4, user.email)
            statement.setString(5, user.password)
            statement.setDate(6, user.birth_date)
            statement.setString(7, user.club)
            statement.setBoolean(8, user.isManager)
            statement.setBoolean(9, user.birthday_notification)
            statement.setDouble(10, user.longitude)
            statement.setDouble(11, user.latitude)
            statement.setInt(12, user.id)
            statement
        }
    }

    fun getMail(userId: Int, username: String?): String? {
        return template.execute { con: Connection ->
            val call = con.prepareCall("{? = call postMail(?, ?)}")
            call.registerOutParameter(1, Types.VARCHAR)
            call.setInt(2, userId)
            call.setString(3, username)

            call.execute()
            call.getString(1)
        }
    }
}


private val x =  { rs: ResultSet, _:Int ->
    User(
        id = rs.getInt("id"),
        email = rs.getString("email"),
        username = rs.getString("username"),
        password = rs.getString("password"),
        firstName = rs.getString("first_name"),
        lastName = rs.getString("last_name"),
        club = rs.getString("club"),
        isManager = rs.getBoolean("is_manager"),
        longitude = rs.getDouble("longitude"),
        latitude = rs.getDouble("latitude"),
        birth_date = rs.getDate("birth_date"),
        birthday_notification = rs.getBoolean("birthday_notification"),
        posts = arrayListOf()
        )
}


private val discover =  { rs: ResultSet, _:Int ->
    Discover(
        id = rs.getInt("id"),
        username = rs.getString("username"),
        firstName = rs.getString("first_name"),
        lastName = rs.getString("last_name")
    )
}

data class Discover(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String
)