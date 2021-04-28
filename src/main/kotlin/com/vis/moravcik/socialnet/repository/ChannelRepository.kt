package com.vis.moravcik.socialnet.repository

import com.vis.moravcik.socialnet.model.Channel
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.*

@Repository
class ChannelRepository(
        val template: JdbcOperations
) {
    fun saveChannel(userId1: Int, userId2: Int): Int? {
        return template.execute { con: Connection ->
            val call = con.prepareCall("{call sendMessage(?, ?, ?)}")
            call.registerOutParameter(3, Types.INTEGER)
            call.setInt(1, userId1)
            call.setInt(2, userId2)
            call.execute()
            call.getInt(3)
        }
    }

    fun selectAll(): MutableList<Channel> {
        return template.query({con: Connection ->
            val statement = con.prepareStatement("select * from channels")
            statement
        }, {rs: ResultSet, _:Int ->
            Channel(
                id = rs.getInt("id"),
                messages = arrayListOf()
            )
        })
    }

    fun delete(id: Int): Int? {
        return template.execute { connection: Connection ->
            val statement = connection.prepareStatement(
                "delete from channels where id=?",
                Statement.RETURN_GENERATED_KEYS
            )
            statement.setInt(1, id)
            statement.executeUpdate()
        }
    }
}