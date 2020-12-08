package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.ChannelUser
import org.springframework.dao.support.DataAccessUtils
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository

@Repository
class ChannelUserRepository(
        val template: JdbcOperations,
        val namedTemplate: NamedParameterJdbcOperations
) {
    // get channel id for two users - if exists
    fun getChannelIdForUsers(user1: Int, user2: Int): Int? {
        val result = DataAccessUtils.singleResult(template.queryForList("""
            select channel_id from channel_users u
            where u.user_id = $user1 and
            exists(select * from channel_users u2 where u.channel_id=u2.channel_id and u2.user_id=$user2)
            """))?.get("channel_id")

        return if (result != null && result is Int) {
            result.toInt()
        } else {
            null
        }
    }

    // get all channels by one user
    fun getChannelIdByUser(userId: Int): MutableList<Int> {
        return template.query("select channel_id from channel_users where user_id=$userId")
        { rs, _ ->
            rs.getInt("channel_id")
        }
    }

    fun getChannelUsersByChannelsAndUserId(userId: Int, ids: List<Int>): MutableList<ChannelUser> {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        return namedTemplate.query("select * from channel_users where user_id != $userId and channel_id in (:ids)", parameters, CHANNEL_USER_MAPPER)
    }

    fun saveUsers(channelId: Int, user1: Int, user2: Int) {
        template.update("insert into channel_users(channel_id, user_id) values ($channelId, $user1)")
        template.update("insert into channel_users(channel_id, user_id) values ($channelId, $user2)")
    }

    fun deleteByChannelId(ids: List<Int>) {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        namedTemplate.update("delete from channel_users where channel_id in (:ids)", parameters)
    }

    fun getChannelsByUsers(ids: List<Int>): MutableList<Int> {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        return namedTemplate.query("select channel_id from channel_users where user_id in (:ids)", parameters
        ) { rs, _ ->
            rs.getInt("channel_id")
        }
    }

    fun getAllByChannelId(id: Int): MutableList<ChannelUser> {
        return template.query("select * from channel_users where channel_id=$id", CHANNEL_USER_MAPPER)
    }
}

// mapping to channel_user object from database
private val CHANNEL_USER_MAPPER: RowMapper<ChannelUser> = RowMapper { rs, _ ->
    ChannelUser(
            id = rs.getInt("id"),
            channelId = rs.getInt("channel_id"),
            userId = rs.getInt("user_id")
    )
}
