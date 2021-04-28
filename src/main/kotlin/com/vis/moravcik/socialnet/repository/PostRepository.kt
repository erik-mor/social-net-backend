package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.controller.CreatePost
import com.vis.moravcik.socialnet.model.GetPost
import com.vis.moravcik.socialnet.model.Post
import com.vis.moravcik.socialnet.model.User
import com.vis.moravcik.socialnet.model.UserPost
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

@Repository
@Component
class PostRepository(
        val template: JdbcOperations,
        val commentRepository: CommentRepository
) {
    fun save(post: CreatePost): Int? {
        return template.execute { connection: Connection ->
            val statement = connection.prepareStatement(
                "insert into posts(user_id, content) values (?, ?)"
            )
            statement.setInt(1, post.user_id)
            statement.setString(2, post.content)
            statement.executeUpdate()
        }
    }

    fun delete(id: Int): Int? {
    return template.execute { connection: Connection ->
        val statement = connection.prepareStatement(
            "delete from posts where id=?"
        )
        statement.setInt(1, id)
        statement.executeUpdate()
        }
    }

    fun update(post: Post) {
        val sql = """
           update posts
            set content=?
            where id = ?
        """
        template.update { con: Connection ->
            val statement = con.prepareStatement(sql)
            statement.setString(1, post.content)
            statement.setInt(2, post.id)
            statement
        }
    }

    // find all posts by user
    fun findAllByUserId(userId: Int): MutableList<UserPost> {
        return template.query({ connection: Connection ->
            val statement = connection.prepareStatement(
                """
                    select u.username, u.first_name, u.last_name, p.content, 
                    (select count(*) from post_comments where post_comments.post_id = p.id) as comment_count, 
                    (select count(*) from post_likes where post_likes.post_id = p.id) as like_count 
                    from posts p join users u on u.id = p.user_id 
                    where u.id = ?
                """
            )
            statement.setInt(1, userId)
            statement
        }, z)
    }

    fun getPostByFollowedUsers(userId: Int): MutableList<GetPost> {
        return template.query({ connection: Connection ->
            val statement = connection.prepareStatement(
                """
                    select u.id as user_id, u.username, u.first_name, u.last_name, p.id as post_id, p.content, 
                    (select count(*) from post_comments where post_comments.post_id = p.id) as comment_count, 
                    (select count(*) from post_likes where post_likes.post_id = p.id) as like_count 
                    from posts p join users u on u.id = p.user_id 
                    where u.id in 
                    (select following_id from follower_following where follower_id = ?)
                """
            )
            statement.setInt(1, userId)
            statement
        }, y)
    }

    fun getPostById(postId: Int): Post? {
        val result = template.query({ connection: Connection ->
            val statement = connection.prepareStatement(
                "select p.id, p.content, (select count(*) from post_likes where post_likes.post_id = p.id) as like_count from posts p where posts_id=? "
            )
            statement.setInt(1, postId)
            statement
        }, x)
        return if (result.isEmpty()) null else {
            val post = result.first()
            val comments = commentRepository.getCommentsByPost(postId)
            post.comments = comments
            post
        }
    }
}

private val x =  { rs: ResultSet, _:Int ->
    Post(
        id = rs.getInt("id"),
        content = rs.getString("content"),
        likes = rs.getInt("like_count"),
        comments = arrayListOf()
    )
}

private val y =  { rs: ResultSet, _:Int ->
    GetPost(
        userId = rs.getInt("user_id"),
        firstName = rs.getString("first_name"),
        lastName = rs.getString("last_name"),
        postId = rs.getInt("post_id"),
        content = rs.getString("content"),
        likes = rs.getInt("like_count"),
        comments = rs.getInt("comment_count")
    )
}

private val z =  { rs: ResultSet, _:Int ->
    UserPost(
        content = rs.getString("content"),
        likes = rs.getInt("like_count"),
        comments = rs.getInt("comment_count")
    )
}
