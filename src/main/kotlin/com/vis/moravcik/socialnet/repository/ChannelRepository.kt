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

    fun getChannelById(id: Int): Channel? {
        val result = template.query("select * from channels where id=${id}", CHANNEL_MAPPER)
        return if (result.isEmpty()) null else result.first()
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

    fun deleteByChannelId(ids: List<Int>) {
        val parameters: SqlParameterSource = MapSqlParameterSource("ids", ids)
        namedTemplate.update("delete from channels where id in (:ids)", parameters)
    }
}

private val CHANNEL_MAPPER: RowMapper<Channel> = RowMapper { rs, _ ->
    Channel(
            id = rs.getInt("id"),
            isArchived = rs.getBoolean("is_archived")
    )
}
