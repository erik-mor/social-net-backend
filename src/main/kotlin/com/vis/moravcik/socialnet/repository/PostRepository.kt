package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.CreatePostDTO
import com.vis.moravcik.socialnet.model.CreateUserDTO
import com.vis.moravcik.socialnet.model.Post
import com.vis.moravcik.socialnet.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
@Component
class PostRepository(
        val template: JdbcOperations,
        val namedTemplate: NamedParameterJdbcOperations
) {
    fun save(post: CreatePostDTO): Int {
        val insertString = "insert into posts(user_id, content) values (?, ?);"

        return template.update(insertString, post.user_id, post.content)
    }

    fun delete(id: Int): Int {
        return template.update("delete from posts where id=$id")
    }

    // delete all posts for list of users
    fun deleteByUsers(ids: List<Int>): Int {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        return namedTemplate.update("delete from posts where user_id in (:ids)", parameters)
    }

    // find all posts by user
    fun findAllByUserId(userId: Int): MutableList<Post> {
        return template.query("select * from posts where id=$userId"
        ) { rs, _ ->
            Post(
                    id = rs.getInt("id"),
                    user_id = rs.getInt("user_id"),
                    content = rs.getString("content")
            )
        }
    }

    fun findPostsByFollowedUsers(ids: List<Int>): MutableList<Post> {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        return namedTemplate.query("select * from posts where user_id in (:ids)"
        ) { rs, _ ->
            Post(
                    id = rs.getInt("id"),
                    user_id = rs.getInt("user_id"),
                    content = rs.getString("content")
            )
        }
    }
}