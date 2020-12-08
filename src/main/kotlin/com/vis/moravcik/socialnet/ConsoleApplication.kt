package com.vis.moravcik.socialnet

import org.json.JSONObject
import java.util.*

val scanner = Scanner(System.`in`)

fun main() {
    var isValid = false

    while (true) {
        println("quit or q to exit")
        println("Enter username: ")
        val username: String = scanner.nextLine()
        if (username == "quit" || username == "q") break
        println("Enter password: ")
        val password: String = scanner.nextLine()

        val response = khttp.post(
                url = "http://localhost:8080/admin/login",
                json = mapOf("username" to username, "password" to password)
        )
        val success = response.jsonObject["success"]
        val message = response.jsonObject["message"]

        if (success is Boolean && success)  {
            isValid = true
            break
        }
        println(message)
    }

    if (isValid) {
        var exit = false
        while (true) {
            println("1> Archive Users   2> Archive Channels    3> Delete Users    4> exit")
            when (scanner.nextLine().toInt()) {
                1 -> archiveUsers()
                2 -> archiveChannels()
                3 -> deleteUsers()
                4 -> exit = true
            }
            if (exit) break
        }
    }
}

fun printUsers(archiving: Boolean = false): Boolean {
    val url = if (archiving) "http://localhost:8080/user/not-archived" else "http://localhost:8080/user/all"
    val response = khttp.get(url)
    val users = response.jsonArray

    return if (users.length() > 0) {
        for (i in 0 until users.length()) {
            val user = users.getJSONObject(i)
            println("ID: ${user["id"]}  username: ${user["username"]}")
        }
        true
    } else {
        val x = if (archiving) "archive" else "delete"
        println("No users to $x")
        false
    }
}

fun printChannels(): Boolean {
    val url = "http://localhost:8080/admin/channels/not-archived"
    val response = khttp.get(url)
    val channels = response.jsonArray

    return if (channels.length() > 0) {
        for (i in 0 until channels.length()) {
            val channel = channels.getJSONObject(i)
            val user1: JSONObject = channel["user1"] as JSONObject
            val user2: JSONObject = channel["user2"] as JSONObject
            println("Channel id: ${channel["id"]} >>> User 1 -> id: ${user1["id"]} ${user1["firstName"]} ${user1["lastName"]} | User 2 -> id: ${user2["id"]} ${user2["firstName"]} ${user2["lastName"]}")
        }
        true
    } else {
        println("No channel to archive.")
        false
    }
}

fun archiveUsers() {
    val isNotEmpty = printUsers(archiving = true)
    if (isNotEmpty) {
        println("Enter space separated values(id) for users to archive")
        process("http://localhost:8080/admin/archive/users")
    }
}

fun archiveChannels() {
    val isNotEmpty = printChannels()
    if (isNotEmpty) {
        println("Enter space separated values(id) for channels to archive")
        process("http://localhost:8080/admin/archive/channels")
    }
}

fun deleteUsers() {
    printUsers()
    println("Enter space separated values(id) for users to delete")
    process("http://localhost:8080/user/batch-delete")
}

fun process(url: String) {
    val query = scanner.nextLine()
    if (query != "quit" && query != "q") {
        val ids: List<Int> = query.trimEnd().split(" ").map { it.toInt() }
        khttp.post(
                url = url,
                json = mapOf("ids" to ids)
        )
    }
}
