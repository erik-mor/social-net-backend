package com.vis.moravcik.socialnet

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
                url = "http://localhost:8080/user/login",
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
            println("1> Archive Users    2> Delete Users    3> exit")
            when (scanner.nextLine().toInt()) {
                1 -> archiveUsers()
                2 -> deleteUsers()
                3 -> exit = true
            }
            if (exit) break
        }
    }
}

fun printUsers(archiving: Boolean = false) {
    val url = if (archiving) "http://localhost:8080/user/not-archived" else "http://localhost:8080/user/"
    val response = khttp.get(url)
    val users = response.jsonArray

    for (i in 0 until users.length()) {
        val user = users.getJSONObject(i)
        println("ID: ${user["id"]}  username: ${user["username"]}")
    }
}

fun archiveUsers() {
    printUsers(archiving = true)
    println("Enter space separated values(id) for users to archive")
    val ids: List<Int> = scanner.nextLine().split(" ").map { it.toInt() }
    khttp.post(
            url = "http://localhost:8080/user/archive",
            json = mapOf("ids" to ids)
    )
}

fun deleteUsers() {
    printUsers()
    println("Enter space separated values(id) for users to delete")
    val ids: List<Int> = scanner.nextLine().split(" ").map { it.toInt() }
    khttp.post(
            url = "http://localhost:8080/user/batch-delete",
            json = mapOf("ids" to ids)
    )
}