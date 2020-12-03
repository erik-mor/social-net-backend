package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.Message
import com.vis.moravcik.socialnet.model.User
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository

@Repository
class MessageRepository(
        val template: JdbcOperations,
        val namedTemplate: NamedParameterJdbcTemplate
) {
    fun saveMessage(message: String, channelId: Int, senderId: Int) {
        template.update("insert into messages(message, channel_id, sender_id) values ('$message', $channelId, $senderId)")
    }

    // get all messages for channel
    fun getMessagesByChannel(channelId: Int): MutableList<Message> {
        return template.query("select * from messages where channel_id=$channelId", MESSAGE_MAPPER)
    }

    // delete all messages for list of users
//    fun deleteByUserIds(ids: List<Int>): Int {
//        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
//        return namedTemplate.update("delete from messages where sender_id in (:ids)", parameters)
//    }

    fun deleteByChannels(ids: List<Int>) {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        namedTemplate.update("delete from messages where channel_id in (:ids)", parameters)
    }


}

private val MESSAGE_MAPPER: RowMapper<Message> = RowMapper { rs, _ ->
    Message(
            id = rs.getInt("id"),
            message = rs.getString("message"),
            senderId = rs.getInt("sender_id"),
            channelId = rs.getInt("channel_id")
    )
}
