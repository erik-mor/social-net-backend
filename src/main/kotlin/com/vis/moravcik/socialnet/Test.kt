package com.vis.moravcik.socialnet

fun main() {

    print("funcke 4a Novy kanal")

    // vsetky kanaly
//    var response = khttp.get("http://localhost:8080/test/getChannels")

//    print(response)
//    print("Volanie pre pouzivatelov ${1} a ${2}, ktory uz kanal maju, a teda sa novy nevytvori")
//    print("Pocet kanalov: ${response.jsonArray.length()}")
//
//    // zavolanie funkcie
     khttp.post(
         url = "http://localhost:8080/messaging/contact",
         json = mapOf("user_id" to 1, "contacted_user_id" to 2)
     )
//
//    // vsetko kanaly znovu po stupsteni funkcie
//    response = khttp.get("http://localhost:8080/test/getChannels")
//    print("Pocet kanalov: ${response.jsonArray.length()}")
//
//    print("Volanie pre pouzivatelov ${1} a ${3}, ktory nameju kanal a teda sa vytvori novy")
//    // zavolanie funkcie
//    khttp.post(
//        url = "http://localhost:8080/messaging/contact",
//        json = mapOf("user_id" to 1, "contacted_user_id" to 3)
//    )
//    // vsetko kanaly znovu po stupsteni funkcie
//    response = khttp.get("http://localhost:8080/test/getChannels")
//    print("Pocet kanalov: ${response.jsonArray.length()}")
//
}