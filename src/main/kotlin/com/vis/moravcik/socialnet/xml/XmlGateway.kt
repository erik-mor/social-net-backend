package com.vis.moravcik.socialnet.xml

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.vis.moravcik.socialnet.model.Post
import com.vis.moravcik.socialnet.model.User
import org.springframework.stereotype.Component
import java.io.File

@Component
class XmlWriter{

    val USERS_FILE_NAME = "xml-archives/user.xml"
    val CHANNELS_FILE_NAME = "xml-archives/channels.xml"

    val xmlMapper = XmlMapper().apply {
        // just for indentation
        enable(SerializationFeature.INDENT_OUTPUT)
    }

    fun saveUsers(xmlUsers: MutableList<XMLUser>) {
        val xmlArchive = File(USERS_FILE_NAME)
        val savedXMLUsers: XMLUsers?

        // if file exists get already archived users
        if (xmlArchive.exists()) {
            savedXMLUsers = readUsers()
            // add new users for archiving
            savedXMLUsers?.users?.addAll(xmlUsers)
        } else {
            savedXMLUsers = XMLUsers(xmlUsers)
        }
        xmlMapper.writeValue(File(USERS_FILE_NAME), savedXMLUsers)
    }

    fun readUsers(): XMLUsers? {
        return xmlMapper.readValue(File(USERS_FILE_NAME), XMLUsers::class.java)
    }

    fun saveChannels(channels: MutableList<XMLChannel>) {
        val xmlArchive = File(CHANNELS_FILE_NAME)
        val savedXMLChannels: XMLChannels?

        // if file exists get already archived users
        if (xmlArchive.exists()) {
            savedXMLChannels = readChannels()

            // add new users for archiving
            savedXMLChannels?.channels?.addAll(channels)
        } else {
            savedXMLChannels = XMLChannels(channels)
        }
       xmlMapper.writeValue(File(CHANNELS_FILE_NAME), savedXMLChannels)
    }

    fun readChannels(): XMLChannels? {
        return xmlMapper.readValue(File(CHANNELS_FILE_NAME), XMLChannels::class.java)
    }
}

// XML objects
data class XMLUsers(
        @set:JsonProperty("User")
        var users: MutableList<XMLUser> = ArrayList()
)

data class XMLUser (
        var id: Int = 0,
        var username: String = "",
        var email: String = "",
        var password: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var is_manager: Boolean = false,
        var longitude: Double? = null,
        var latitude: Double? = null,
        var posts: List<XMLPost>? = ArrayList()
)
data class XMLPost (
        val id: Int = 0,
        var user_id: Int = 0,
        var content: String = ""
)

data class XMLChannels(
        val channels: MutableList<XMLChannel> = ArrayList()
)

data class XMLChannel (
        val id: Int = 0,
        val user1: Int = 0,
        val user2: Int = 0,
        val messages: List<XMLMessage> = ArrayList()
)

data class XMLMessage (
        val id: Int = 0,
        val sender_id: Int= 0,
        val message: String = ""
)
