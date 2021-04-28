package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.controller.CreateComment
import com.vis.moravcik.socialnet.model.Comment
import com.vis.moravcik.socialnet.model.Post
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

@Repository
class CommentRepository(
    val template: JdbcOperations
) {
    fun save(comment: CreateComment): Int? {
        return template.execute { connection: Connection ->
            val statement = connection.prepareStatement(
                "insert into post_comments(user_id, post_id, content) values (?, ?, ?)"
            )
            statement.setInt(1, comment.user_id)
            statement.setInt(2, comment.post_id)
            statement.setString(3, comment.content)
            statement.executeUpdate()
        }
    }

    fun getCommentsByPost(postId: Int): MutableList<Comment> {
        return template.query({ connection: Connection ->
            val statement = connection.prepareStatement(
                "select * from post_comments where post_id = ?"
            )
            statement.setInt(1, postId)
            statement
        }, x)
    }

    fun update(comment: Comment) {
        val sql = """
           update post_comments
            set content=?
            where id = ?
        """
        template.update { con: Connection ->
            val statement = con.prepareStatement(sql)
            statement.setString(1, comment.content)
            statement.setInt(2, comment.id)
            statement
        }
    }

    fun delete(id: Int): Int? {
        return template.execute { connection: Connection ->
            val statement = connection.prepareStatement(
                "delete from post_comments where id=?"
            )
            statement.setInt(1, id)
            statement.executeUpdate()
        }
    }
}

private val x =  { rs: ResultSet, _:Int ->
    Comment(
        id = rs.getInt("id"),
        content = rs.getString("content")
    )
}
