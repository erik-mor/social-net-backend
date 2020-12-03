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

    val USERS_FILE_NAME = "xml-archive/user.xml"
    val xmlMapper = XmlMapper().apply {
        // just for indentation
        enable(SerializationFeature.INDENT_OUTPUT)
    }

    fun saveUsers(xmlUsers: MutableList<XMLUser>) {
        val xmlArchive = File("user.xml")
        val savedXMLUsers: XMLUsers?

        // if file exists get already archived users
        if (xmlArchive.exists()) {
            savedXMLUsers = read()
            // add new users for archiving
            savedXMLUsers?.users?.addAll(xmlUsers)
        } else {
            savedXMLUsers = XMLUsers(xmlUsers)
        }
        xmlMapper.writeValue(File(USERS_FILE_NAME), savedXMLUsers)
    }

    fun read(): XMLUsers? {
        return xmlMapper.readValue(File(USERS_FILE_NAME), XMLUsers::class.java)
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
        var posts: List<XMLPost>
) {
    constructor(user: User, posts: List<XMLPost>): this(
            user.id,
            user.username,
            user.email,
            user.password,
            user.firstName,
            user.lastName ,
            user.isManager,
            user.longitude,
            user.latitude,
            posts
    )
}

data class XMLPost (
        val id: Int,
        var user_id: Int,
        var content: String
) {
    constructor(post: Post): this(
            post.id,
            post.user_id,
            post.content
    )
}

