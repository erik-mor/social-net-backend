package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.Message
import com.vis.moravcik.socialnet.model.User
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.ResultSet

@Repository
class MessageRepository(
        val template: JdbcOperations
) {
    fun saveMessage(message: String, channelId: Int, senderId: Int): Int? {
        return template.execute { connection: Connection ->
            val statement = connection.prepareStatement(
                "insert into messages(message, channel_id, sender_id) values (?, ?, ?)"
            )
            statement.setString(1, message)
            statement.setInt(2, channelId)
            statement.setInt(3, senderId)
            statement.executeUpdate()
        }
    }

    // get all messages for channel
    fun getMessagesByChannel(channelId: Int): MutableList<Message> {
        return template.query({ connection: Connection ->
            val statement = connection.prepareStatement(
                "select * from messages where channel_id=? "
            )
            statement.setInt(1, channelId)
            statement
        }, MESSAGE_MAPPER)
    }
}

private val MESSAGE_MAPPER =  { rs: ResultSet, _:Int ->
    Message(
            id = rs.getInt("id"),
            message = rs.getString("message"),
            userId = rs.getInt("user_id"),
            channelId = rs.getInt("channel_id")
    )
}
