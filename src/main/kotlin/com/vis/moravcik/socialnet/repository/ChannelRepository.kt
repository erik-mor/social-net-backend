package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.Channel
import com.vis.moravcik.socialnet.model.ChannelUser
import org.springframework.dao.support.DataAccessUtils
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class ChannelRepository(
        val template: JdbcOperations,
        val namedTemplate: NamedParameterJdbcOperations
) {
    fun saveChannel(): Int {
        val holder = GeneratedKeyHolder()
        template.update({ connection ->
            connection.prepareStatement("insert into channels default values;", Statement.RETURN_GENERATED_KEYS)
        }, holder )
        return holder.keyList[0]["id"].toString().toInt()
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
    fun getChannelsByUserId(userId: Int): MutableList<Int> {
        return template.query("select channel_id from channel_users where user_id=$userId")
        { rs, _ ->
            rs.getInt("channel_id")
        }
    }

    fun getChannelUsersByChannelIdAndUserId(userId: Int, ids: List<Int>): MutableList<ChannelUser> {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        return namedTemplate.query("select * from channel_users where user_id != $userId and channel_id in (:ids)", parameters, CHANNEL_USER_MAPPER)
    }

    fun getChannelById(id: Int): Channel? {
        val result = template.query("select * from channels where id=${id}", CHANNEL_MAPPER)
        return if (result.isEmpty()) null else result.first()
    }

    fun saveUsers(channelId: Int, user1: Int, user2: Int) {
        template.update("insert into channel_users(channel_id, user_id) values ($channelId, $user1)")
        template.update("insert into channel_users(channel_id, user_id) values ($channelId, $user2)")
    }

    fun deleteByChannels(ids: List<Int>) {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        namedTemplate.update("delete from channel_users where channel_id in (:ids)", parameters)
        namedTemplate.update("delete from channels where id in (:ids)", parameters)
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

    fun setArchived(is_archived: Boolean, ids: List<Int>) {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        namedTemplate.update("update channels set is_archived=$is_archived where id in (:ids);", parameters)
    }

    fun findAllNotArchived(): MutableList<Int> {
        return template.query("select id from channels where is_archived=false"
        ) { rs, _ ->
            rs.getInt("id")
        }
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

private val CHANNEL_MAPPER: RowMapper<Channel> = RowMapper { rs, _ ->
    Channel(
            id = rs.getInt("id"),
            isArchived = rs.getBoolean("is_archived")
    )
}
