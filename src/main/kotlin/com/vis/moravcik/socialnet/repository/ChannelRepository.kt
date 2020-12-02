package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.ChannelUser
import com.vis.moravcik.socialnet.model.User
import org.springframework.dao.support.DataAccessUtils
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

// repository for channel_users and channels table
@Repository
class ChannelRepository(
        val template: JdbcOperations,
        val namedTemplate: NamedParameterJdbcOperations
) {
    fun saveChannel(): Int {
        val holder = GeneratedKeyHolder()
        template.update({ connection ->
            connection.prepareStatement("insert into channels default values;", Statement.RETURN_GENERATED_KEYS)
        }, holder)
        return holder.key!!.toInt()
    }

    // get channel id for two users - if exists
    fun getChannelByUsers(user1: Int, user2: Int): Int? {
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
    fun getChannelsByUserId(userId: Int): MutableList<ChannelUser> {
        return template.query("select * from channel_users where user_id=$userId", CHANNEL_MAPPER)
    }

    fun saveUsers(channelId: Int, user1: Int, user2: Int) {
        template.update("insert into channel_users(channel_id, user_id) values ($channelId, $user1)")
        template.update("insert into channel_users(channel_id, user_id) values ($channelId, $user2)")
    }

    // delete all rows for list of users
    fun deleteByUserIds(ids: List<Int>) {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        val channels = namedTemplate.query("select * from channel_users where user_id in (:id)", parameters, CHANNEL_MAPPER)

        val deleteChannelsParameters: SqlParameterSource = MapSqlParameterSource("ids", channels.map { it.channelId })
        val deleteChannelUsersParameters: SqlParameterSource = MapSqlParameterSource("ids", channels.map { it.id })

        namedTemplate.update("delete from channel_users where id in (:ids)", deleteChannelUsersParameters)
        namedTemplate.update("delete from channels where id in (:ids)", deleteChannelsParameters)
    }
}

// mapping to channel_user object from database
private val CHANNEL_MAPPER: RowMapper<ChannelUser> = RowMapper { rs, _ ->
    ChannelUser(
            id = rs.getInt("id"),
            channelId = rs.getInt("channel_id"),
            userId = rs.getInt("user_id")
    )
}
