package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.FollowerFollowing
import org.postgresql.core.SqlCommand
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

@Repository
@Component
class FollowerRepository(
        val template: JdbcOperations,
        val namedTemplate: NamedParameterJdbcOperations
) {
    fun save(followerFollowing: FollowerFollowing): Int? {
        val insertString = "insert into follower_following(follower_id, following_id) values (?, ?);"

        return template.execute { connection: Connection ->
            val statement = connection.prepareStatement(insertString)
            statement.setInt(1, followerFollowing.followerId)
            statement.setInt(2, followerFollowing.followingId)
            statement.executeUpdate()
        }
    }

    // check if user with id = followerId is following user with id = followingId
    fun findByFollowerIdAndFollowingId(followerId: Int, followingId: Int): FollowerFollowing? {
        val result = template.query("select * from follower_following where follower_id=$followerId and following_id=$followingId")
        { rs, _ ->
            FollowerFollowing(
                    followingId = rs.getInt("following_id"),
                    followerId = rs.getInt("follower_id")
            )
        }
        return if (result.isEmpty()) null else result.first()
    }
}
