package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.Like
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.stereotype.Repository
import java.sql.Connection

@Repository
class LikeRepository(
        val template: JdbcOperations
) {
    fun save(like: Like): Int? {
        return template.execute { connection: Connection ->
            val statement = connection.prepareStatement(
                "insert into likes(user_id, post_id) values (?, ?)"
            )
            statement.setInt(1, like.userId)
            statement.setInt(2, like.postId)
            statement.executeUpdate()
        }
    }

    fun delete(like: Like): Int? {
        return template.execute { connection: Connection ->
            val statement = connection.prepareStatement(
                "delete from post_likes where user_id=? and post_id=?"
            )
            statement.setInt(1, like.userId)
            statement.setInt(2, like.postId)

            statement.executeUpdate()
        }
    }


}