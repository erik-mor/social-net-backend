package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.CreateFollowerFollowing
import com.vis.moravcik.socialnet.model.FollowerFollowing
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
@Component
class FollowerRepository(
        val template: JdbcOperations,
        val namedTemplate: NamedParameterJdbcOperations
) {
    fun save(followerFollowing: CreateFollowerFollowing): Int {
        val insertString = "insert into follower_following(follower_id, following_id) values (?, ?);"

        return template.update(insertString, followerFollowing.followerId, followerFollowing.followingId)
    }

    // check if user with id = followerId is following user with id = followingId
    fun findByFollowerIdAndFollowingId(followerId: Int, followingId: Int): FollowerFollowing? {
        val result = template.query("select * from follower_following where follower_id=$followerId and following_id=$followingId")
        { rs, _ ->
            FollowerFollowing(
                    id = rs.getInt("id"),
                    followingId = rs.getInt("following_id"),
                    followerId = rs.getInt("follower_id")
            )
        }
        return if (result.isEmpty()) null else result.first()
    }

    // find list of users that user with id = followerId is following
    fun findFollowingByFollowerId(followerId: Int): MutableList<Int> {
        return template.query("select following_id from follower_following where follower_id=$followerId"
        ) { rs, _ ->
            rs.getInt("following_id")
        }
    }

    // delete following records for list of users
    fun deleteByUserIds(ids: List<Int>): Int {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        return namedTemplate.update("delete from follower_following where follower_id in (:ids) or following_id in (:ids)", parameters)
    }
}
